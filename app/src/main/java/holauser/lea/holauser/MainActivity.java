package holauser.lea.holauser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button btnPlay;
    RelativeLayout rlSelectContent;
    TextView chronometer;

    CheckBox cuenco;
    CheckBox koshi;

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv10;
    TextView tv15;
    TextView tv20;
    TextView tv25;
    TextView tv30;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btn_play);
        rlSelectContent = (RelativeLayout) findViewById(R.id.rl_content_select);
        chronometer = (TextView) findViewById(R.id.chronometer);

        cuenco = (CheckBox) findViewById(R.id.cb_cuenco);
        koshi = (CheckBox) findViewById(R.id.cb_koshi);

        tv3 = (TextView) findViewById(R.id.btnTime3);
        tv5 = (TextView) findViewById(R.id.btnTime5);
        tv7 = (TextView) findViewById(R.id.btnTime7);
        tv10 = (TextView) findViewById(R.id.btnTime10);
        tv15 = (TextView) findViewById(R.id.btnTime15);
        tv20 = (TextView) findViewById(R.id.btnTime20);

        tv1 = (TextView) findViewById(R.id.btnTime1);
        tv2 = (TextView) findViewById(R.id.btnTime2);
        tv4 = (TextView) findViewById(R.id.btnTime4);
        tv6 = (TextView) findViewById(R.id.btnTime6);
        tv25 = (TextView) findViewById(R.id.btnTime25);
        tv30 = (TextView) findViewById(R.id.btnTime30);

        tv3.setOnClickListener(btnTimeListener);
        tv5.setOnClickListener(btnTimeListener);
        tv7.setOnClickListener(btnTimeListener);
        tv10.setOnClickListener(btnTimeListener);
        tv15.setOnClickListener(btnTimeListener);
        tv20.setOnClickListener(btnTimeListener);
        tv1.setOnClickListener(btnTimeListener);
        tv2.setOnClickListener(btnTimeListener);
        tv4.setOnClickListener(btnTimeListener);
        tv6.setOnClickListener(btnTimeListener);
        tv25.setOnClickListener(btnTimeListener);
        tv30.setOnClickListener(btnTimeListener);

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
                countDownTimer = new CountDownTimer(((GlobalVars)getApplicationContext()).getTiempo() * 60000, 1000) {

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
                Toast.makeText(getApplicationContext(), "Playing every " + gb.getTiempo() + " minutes", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, SoundService.class));
            }
        }
    };

    View.OnClickListener btnTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
            globalVariable.setTiempo(Integer.parseInt((String)view.getTag()));
            resetTimeBtns();
            view.setBackgroundResource(R.drawable.boton_selected);
        }
    };

    private void resetTimeBtns () {
        tv3.setBackgroundResource(R.drawable.boton);
        tv5.setBackgroundResource(R.drawable.boton);
        tv7.setBackgroundResource(R.drawable.boton);
        tv10.setBackgroundResource(R.drawable.boton);
        tv15.setBackgroundResource(R.drawable.boton);
        tv20.setBackgroundResource(R.drawable.boton);
        tv1.setBackgroundResource(R.drawable.boton);
        tv2.setBackgroundResource(R.drawable.boton);
        tv4.setBackgroundResource(R.drawable.boton);
        tv6.setBackgroundResource(R.drawable.boton);
        tv25.setBackgroundResource(R.drawable.boton);
        tv30.setBackgroundResource(R.drawable.boton);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, SoundService.class));
    }

}
