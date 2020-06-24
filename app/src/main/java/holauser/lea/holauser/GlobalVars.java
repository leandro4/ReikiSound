package holauser.lea.holauser;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

public class GlobalVars extends MultiDexApplication {
    private int audio = R.raw.cuenco;
    private int tiempo = 3;
    private boolean isPlaying = false;
    private boolean playMusic = false;
    private Uri musicToPlay = null;
    private float volume = 1f;
    public static final String CHANNEL_ID = "default_channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "PlayerChannel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public boolean isPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public void setSonido (int sonido) {
        audio = sonido;
    }

    public int getSonido () {
        return audio;
    }

    public void setTiempo (int t) {
        tiempo = t;
    }

    public int getTiempo () {
        return tiempo;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Uri getMusicToPlay() {
        return musicToPlay;
    }

    public void setMusicToPlay(Uri musicToPlay) {
        this.musicToPlay = musicToPlay;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
