package holauser.lea.holauser.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.services.SoundService;
import holauser.lea.holauser.ui.custom.NumberPickerView;
import holauser.lea.holauser.util.Animations;

public class MainActivity extends Activity {

    @BindView(R.id.btn_play)
    ImageView btnPlay;
    @BindView(R.id.rl_content_select)
    LinearLayout rlSelectContent;
    @BindView(R.id.chronometer)
    TextView chronometer;
    @BindView(R.id.numberPicker)
    NumberPickerView numberPicker;
    @BindView(R.id.volumePicker)
    NumberPickerView volumePicker;
    @BindView(R.id.cb_enable_music)
    CheckBox music;
    @BindView(R.id.fondo)
    ConstraintLayout root;

    BroadcastReceiver receiver;

    public static final int AUDIO_SELECTION = 1000;
    String link = "https://sites.google.com/view/reikisound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccentExtraLigth));
        }

        volumePicker.setMinValue(1);
        volumePicker.setMaxValue(10);
        volumePicker.setValue(10);

        music.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) showSelectAudioDialog();
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

    @OnClick (R.id.btn_play)
    public void onPlayClick(View v) {
        Animations.animateScale(v);

        GlobalVars gb = (GlobalVars)getApplicationContext();

        if (gb.isPlaying()) {
            stopService(new Intent(MainActivity.this, SoundService.class));
            setStopedMode(true);
        }

        else {
            gb.setTiempo(numberPicker.getValue());
            gb.setVolume((float) (volumePicker.getValue() / 10.0));
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

    @OnClick (R.id.btnPage)
    public void onDonateClick(View v) {
        Animations.animateScale(v);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
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
        setStopedMode(!((GlobalVars)getApplicationContext()).isPlaying());
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

                String title = getString(R.string.selected_audio);
                String message = getString(R.string.selected_audio_body);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("Ok", (dialog, which) -> selectAudio());
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
        String title = getString(R.string.select_audio_title);
        String message = getString(R.string.select_audio_body);
        String positiveBtn = getString(R.string.select_audio_select);
        String defaultBtn = getString(R.string.select_audio_default);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtn, (dialog, which) -> selectAudio());
        builder.setNegativeButton(defaultBtn, (dialog, which) -> {
            GlobalVars gb = (GlobalVars)getApplicationContext();
            gb.setMusicToPlay(null);
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void selectAudio() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, AUDIO_SELECTION);
    }
}
