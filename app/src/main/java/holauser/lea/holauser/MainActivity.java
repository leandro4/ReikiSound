package holauser.lea.holauser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @BindView(R.id.btn_play)
    Button btnPlay;
    @BindView(R.id.rl_content_select)
    RelativeLayout rlSelectContent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(3);

        btnPlay.setOnClickListener(btnPlayListener);

        cuenco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GlobalVars globalVariable = (GlobalVars) getApplicationContext();

                if (globalVariable.getSonido() == R.raw.cuenco) {
                    cuenco.setChecked(true);
                } else {
                    globalVariable.setSonido(R.raw.cuenco);
                    koshi.setChecked(false);
                    triangle.setChecked(false);
                }
            }
        });

        koshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GlobalVars globalVariable = (GlobalVars) getApplicationContext();

                if (globalVariable.getSonido() == R.raw.koshi) {
                    koshi.setChecked(true);
                } else {
                    globalVariable.setSonido(R.raw.koshi);
                    cuenco.setChecked(false);
                    triangle.setChecked(false);
                }
            }
        });

        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GlobalVars globalVariable = (GlobalVars) getApplicationContext();

                if (globalVariable.getSonido() == R.raw.triangle) {
                    triangle.setChecked(true);
                } else {
                    globalVariable.setSonido(R.raw.triangle);
                    cuenco.setChecked(false);
                    koshi.setChecked(false);
                }
            }
        });
    }

    View.OnClickListener btnPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
    };

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, SoundService.class));
    }

}
