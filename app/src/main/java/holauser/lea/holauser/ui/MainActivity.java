package holauser.lea.holauser.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Token;
import com.mercadopago.util.JsonUtil;

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

    public static final int LOCATION_PERMISSION_REQUEST = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(3);

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
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);
            }
        } else {
            donate();
        }
    }

    private void donate() {
        DonateDialogFragment dialog = new DonateDialogFragment();
        dialog.setActivity(this);
        dialog.show(getFragmentManager(), "donate");
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, SoundService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MercadoPago.CARD_VAULT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);
                Issuer issuer = JsonUtil.getInstance().fromJson(data.getStringExtra("issuer"), Issuer.class);
                Token token = JsonUtil.getInstance().fromJson(data.getStringExtra("token"), Token.class);

                Toast.makeText(this, getString(R.string.mercadopago_success), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.mercadopago_fail), Toast.LENGTH_SHORT).show();
                if ((data != null) && (data.hasExtra("mpException"))) {
                    MPException exception = JsonUtil.getInstance().fromJson(data.getStringExtra("mpException"), MPException.class);
                    Log.e("MercagoPago", "Mensaje: " + exception.getApiException().getError());
                    Log.e("MercagoPago", "Detalle: " + exception.getApiException().getMessage());
                    Log.e("MercagoPago", "Api status :" + exception.getApiException().getStatus());
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    donate();
                }
            }
        }
    }

    private void selectAudioFile() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload,1234);
    }
}
