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
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.language.EnglishLanguage;
import holauser.lea.holauser.language.FrenchLanguage;
import holauser.lea.holauser.language.ItalianLanguage;
import holauser.lea.holauser.language.LanguageStrategy;
import holauser.lea.holauser.language.PortugeseLanguage;
import holauser.lea.holauser.language.RussianLanguage;
import holauser.lea.holauser.language.SpanishLanguage;
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
    @BindView(R.id.tvMusic)
    TextView tvMusicName;

    @BindView(R.id.languageSp)
    ImageView languageSp;

    BroadcastReceiver receiver;

    public static final int AUDIO_SELECTION = 1000;
    String link = "https://sites.google.com/view/reikisound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setStatusBarColor();

        volumePicker.setMinValue(1);
        volumePicker.setMaxValue(10);
        volumePicker.setValue(10);

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
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccentExtraLigth));
        }
    }

    @OnClick(R.id.languageSp)
    public void languageClick(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.en:
                        ((GlobalVars) getApplicationContext()).languageStrategy = new EnglishLanguage();
                        languageSp.setImageResource(R.drawable.en);
                        break;
                    case R.id.sp:
                        ((GlobalVars) getApplicationContext()).languageStrategy = new SpanishLanguage();
                        languageSp.setImageResource(R.drawable.sp);
                        break;
                    case R.id.br:
                        ((GlobalVars) getApplicationContext()).languageStrategy = new PortugeseLanguage();
                        languageSp.setImageResource(R.drawable.br);
                        break;
                    case R.id.it:
                        ((GlobalVars) getApplicationContext()).languageStrategy = new ItalianLanguage();
                        languageSp.setImageResource(R.drawable.it);
                        break;
                    case R.id.fr:
                        ((GlobalVars) getApplicationContext()).languageStrategy = new FrenchLanguage();
                        languageSp.setImageResource(R.drawable.fr);
                        break;
                    case R.id.ru:
                        ((GlobalVars) getApplicationContext()).languageStrategy = new RussianLanguage();
                        languageSp.setImageResource(R.drawable.ru);
                        break;
                    default: break;
                }
                translateApp();
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.language_menu, popup.getMenu());
        popup.show();
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

                LanguageStrategy translator = ((GlobalVars) getApplicationContext()).languageStrategy;
                String title = ((GlobalVars) getApplicationContext()).wasTranslated ? translator.getString("selected_audio") :
                        getString(R.string.selected_audio);
                String message = ((GlobalVars) getApplicationContext()).wasTranslated ? translator.getString("selected_audio_body") :
                        getString(R.string.selected_audio_body);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(title);
                builder.setMessage(message);
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
        LanguageStrategy translator = ((GlobalVars) getApplicationContext()).languageStrategy;
        String title = ((GlobalVars) getApplicationContext()).wasTranslated ? translator.getString("select_audio_title") :
                getString(R.string.select_audio_title);
        String message = ((GlobalVars) getApplicationContext()).wasTranslated ? translator.getString("select_audio_body") :
                getString(R.string.select_audio_body);
        String positiveBtn = ((GlobalVars) getApplicationContext()).wasTranslated ? translator.getString("select_audio_select") :
                getString(R.string.select_audio_select);
        String defaultBtn = ((GlobalVars) getApplicationContext()).wasTranslated ? translator.getString("select_audio_default") :
                getString(R.string.select_audio_default);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectAudio();
            }
        });
        builder.setNegativeButton(defaultBtn, new DialogInterface.OnClickListener() {
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
        startActivityForResult(intent_upload, AUDIO_SELECTION);
    }

    private void translateApp() {
        ((GlobalVars) getApplicationContext()).wasTranslated = true;
        LanguageStrategy translator = ((GlobalVars) getApplicationContext()).languageStrategy;

        ((TextView) findViewById(R.id.cuadro_lapso)).setText(translator.getString("time_title"));
        ((TextView) findViewById(R.id.tv_volume)).setText(translator.getString("tv_volume"));
        chronometer.setText(translator.getString("remaining"));
        music.setText(translator.getString("enable_music"));
    }
}
