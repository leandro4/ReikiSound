package holauser.lea.holauser;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.Uri;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

public class ReikiSound extends MultiDexApplication {

    private Uri musicToPlay = null;

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

    public Uri getMusicToPlay() {
        return musicToPlay;
    }

    public void setMusicToPlay(Uri musicToPlay) {
        this.musicToPlay = musicToPlay;
    }

}
