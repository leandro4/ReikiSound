package holauser.lea.holauser.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.services.SoundService;

public class MainActivity extends Activity {

    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.rl_content_select)
    LinearLayout rlSelectContent;
    @BindView(R.id.chronometer)
    TextView chronometer;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    @BindView(R.id.cb_cuenco)
    CheckBox cuenco;
    @BindView(R.id.cb_koshi)
    CheckBox koshi;
    @BindView(R.id.cb_triangle)
    CheckBox triangle;
    @BindView(R.id.cb_enable_music)
    CheckBox music;

    CountDownTimer countDownTimer;

    public static final int PAYPAL_PAYMENT = 12345;
    public static final int AUDIO_SELECTION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(3);

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showSelectAudioDialog();
            }
        });
    }

    @OnClick (R.id.cb_cuenco)
    public void onCuencoClick(View v) {
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();

        if (globalVariable.getSonido() == R.raw.cuenco) {
            cuenco.setChecked(true);
        } else {
            globalVariable.setSonido(R.raw.cuenco);
            koshi.setChecked(false);
            triangle.setChecked(false);
        }
    }

    @OnClick (R.id.cb_koshi)
    public void onKoshiClick(View v) {
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();

        if (globalVariable.getSonido() == R.raw.koshi) {
            koshi.setChecked(true);
        } else {
            globalVariable.setSonido(R.raw.koshi);
            cuenco.setChecked(false);
            triangle.setChecked(false);
        }
    }

    @OnClick (R.id.cb_triangle)
    public void onTriangleClick(View v) {
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();

        if (globalVariable.getSonido() == R.raw.triangle) {
            triangle.setChecked(true);
        } else {
            globalVariable.setSonido(R.raw.triangle);
            cuenco.setChecked(false);
            koshi.setChecked(false);
        }
    }

    @OnClick (R.id.btn_play)
    public void onPlayClick(View v) {
        GlobalVars gb = (GlobalVars)getApplicationContext();

        if (gb.isPlaying()) {
            countDownTimer.cancel();
            stopService(new Intent(MainActivity.this, SoundService.class));
            rlSelectContent.setVisibility(View.VISIBLE);
            chronometer.setVisibility(View.GONE);
            btnPlay.setText("PLAY");
        }

        else {
            gb.setTiempo(numberPicker.getValue());

            countDownTimer = new CountDownTimer(numberPicker.getValue() * 60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    chronometer.setText("Remaining:\n " + millisUntilFinished / 1000 + "s");
                }

                public void onFinish() {
                    countDownTimer.start();
                }
            };

            countDownTimer.start();
            rlSelectContent.setVisibility(View.GONE);
            chronometer.setVisibility(View.VISIBLE);
            btnPlay.setText("STOP");
            if (music.isChecked())
                gb.setPlayMusic(true);
            else
                gb.setPlayMusic(false);
            startService(new Intent(MainActivity.this, SoundService.class));
        }
    }

    @OnClick (R.id.btn_donate)
    public void onDonateClick(View v) {
        DonateDialogFragment dialog = new DonateDialogFragment();
        dialog.setActivity(this);
        dialog.show(getFragmentManager(), "donate");
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, SoundService.class));
        stopService(new Intent(this, PayPalService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            if (MediaPlayer.create(this, uri) == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.selected_audio));
                builder.setMessage(getString(R.string.selected_audio_body));
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectAudio();
                    }
                });
                builder.show();
            } else {
                GlobalVars gb = (GlobalVars) getApplicationContext();
                gb.setMusicToPlay(uri);
            }

        } else if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Toast.makeText(this, getString(R.string.mercadopago_success), Toast.LENGTH_SHORT).show();
                    Log.i("sampleapp", confirm.toJSONObject().toString(4));
                } catch (JSONException e) {
                    Log.e("sampleapp", "no confirmation data: ", e);
                }
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, getString(R.string.mercadopago_fail), Toast.LENGTH_SHORT).show();
            Log.i("sampleapp", "Invalid payment / config set");
        }

    }

    private void showSelectAudioDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_audio_title));
        builder.setMessage(getString(R.string.select_audio_body));
        builder.setPositiveButton(getString(R.string.select_audio_select), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectAudio();
            }
        });
        builder.setNegativeButton(getString(R.string.select_audio_default), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobalVars gb = (GlobalVars)getApplicationContext();
                gb.setMusicToPlay(null);
            }
        });
        builder.show();
    }

    private void selectAudio() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,AUDIO_SELECTION);
    }
}
