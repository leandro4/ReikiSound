package holauser.lea.holauser.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.services.SoundService;

public class MainActivity extends Activity {

    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.rl_content_select)
    LinearLayout rlSelectContent;
    @BindView(R.id.chronometer)
    TextView chronometer;

    @BindView(R.id.numberPicker)
    NumberPickerView numberPicker;

    @BindView(R.id.cb_cuenco)
    CheckBox cuenco;
    @BindView(R.id.cb_koshi)
    CheckBox koshi;
    @BindView(R.id.cb_triangle)
    CheckBox triangle;
    @BindView(R.id.cb_enable_music)
    CheckBox music;
    @BindView(R.id.tvMusic)
    TextView tvMusicName;

    BroadcastReceiver receiver;

    public static final int AUDIO_SELECTION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setStatusBarColor();

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showSelectAudioDialog();
            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra(SoundService.REIKI_MESSAGE);
                chronometer.setText(time);
            }
        };

        setStopedMode(!((GlobalVars)getApplicationContext()).isPlaying());
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
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
            stopService(new Intent(MainActivity.this, SoundService.class));
            setStopedMode(true);
        }

        else {
            gb.setTiempo(numberPicker.getValue());
            setStopedMode(false);

            if (music.isChecked())
                gb.setPlayMusic(true);
            else
                gb.setPlayMusic(false);

            registerBroadcastReceiver(true);
            startService(new Intent(MainActivity.this, SoundService.class));
        }
    }

    private void setStopedMode(boolean stoped) {
        rlSelectContent.setVisibility(stoped ? View.VISIBLE : View.GONE);
        chronometer.setVisibility(!stoped ? View.VISIBLE : View.GONE);
        btnPlay.setImageResource(stoped ? R.drawable.ic_play : R.drawable.ic_stop);
    }

    @OnClick (R.id.btn_donate)
    public void onDonateClick(View v) {
        DonateDialogFragment dialog = new DonateDialogFragment();
        dialog.setActivity(this);
        dialog.show(getFragmentManager(), "donate");
    }

    @Override
    public void onStop () {
        super.onStop();
        registerBroadcastReceiver(false);
    }

    @Override
    public void onResume () {
        super.onResume();
        registerBroadcastReceiver(true);
    }

    private void registerBroadcastReceiver(boolean register) {
        if (register) {
            LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(SoundService.BROADCAST_REIKI));
        }
        else
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
        } else if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            music.setChecked(false);
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
        builder.setCancelable(false);
        builder.show();
    }

    private void selectAudio() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,AUDIO_SELECTION);
    }
}
