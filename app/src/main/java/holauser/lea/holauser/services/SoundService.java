package holauser.lea.holauser.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;

public class SoundService extends Service {
    MediaPlayer rep;
    MediaPlayer repMusic;
    Timer timer = new Timer();

    @Override
    public void onCreate() {
        super.onCreate();
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

        Toast.makeText(this,"Stoped", Toast.LENGTH_SHORT).show();
        timer.cancel();
        ((GlobalVars)getApplicationContext()).setPlaying(false);
        try {
            repMusic.stop();
            repMusic.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
     public IBinder onBind(Intent intencion) {
    return null;
}
}

