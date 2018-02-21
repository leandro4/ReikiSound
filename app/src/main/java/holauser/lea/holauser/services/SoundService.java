package holauser.lea.holauser.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.ui.MainActivity;

public class SoundService extends Service {

    MediaPlayer rep;
    MediaPlayer repMusic;
    Timer timer = new Timer();

    public static final int NOTIFICATION_ID = 1414;

    CountDownTimer countDownTimer;

    LocalBroadcastManager broadcaster;
    static final public String BROADCAST_REIKI = "broadcast_reiki";
    static final public String REIKI_MESSAGE = "reiki_message";

    public void sendResult(String title, String time) {
        Intent intent = new Intent(BROADCAST_REIKI);
        intent.putExtra(REIKI_MESSAGE, title + "\n"+ time);
        broadcaster.sendBroadcast(intent);

        makeNotification(this, time);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
        rep = MediaPlayer.create(this, globalVariable.getSonido());
        int tiempoEspera = 1000 * globalVariable.getTiempo() * 60;
        timer.purge();

        if (globalVariable.getMusicToPlay() == null)
            repMusic = MediaPlayer.create(this, R.raw.relaxing1);
        else
            repMusic = MediaPlayer.create(this, globalVariable.getMusicToPlay());

        if (globalVariable.isPlayMusic()) {
            repMusic.setLooping(true);
            repMusic.start();
        }

        countDownTimer = new CountDownTimer(tiempoEspera, 1000) {

            public void onTick(long millisUntilFinished) {
                sendResult(getString(R.string.remaining), millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                countDownTimer.start();
            }
        };
        countDownTimer.start();

        TimerTask timerTask = new TimerTask() {
            public void run() {
                rep.start();
                globalVariable.setPlaying(true);

            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, tiempoEspera);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        timer.cancel();
        countDownTimer.cancel();
        ((GlobalVars)getApplicationContext()).setPlaying(false);
        try {
            repMusic.stop();
            repMusic.release();
            notificationManager.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void makeNotification(Context context, String time) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setSubText(context.getString(R.string.service_subtitle))
                .setContentTitle(context.getString(R.string.remaining))
                .setContentText(time)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.reiki)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon));

        Notification n;

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, n);
    }

    @Override
     public IBinder onBind(Intent intencion) {
    return null;
}
}

