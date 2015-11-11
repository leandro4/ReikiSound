package holauser.lea.holauser;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SoundService extends Service {
    MediaPlayer rep;
    Timer timer = new Timer();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        final GlobalVars globalVariable = (GlobalVars) getApplicationContext();
        rep = MediaPlayer.create(this, globalVariable.getSonido());
        int tiempoEspera = 1000 * globalVariable.getTiempo();
        timer.purge();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                rep.start();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, tiempoEspera);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
        timer.cancel();
    }

    @Override
     public IBinder onBind(Intent intencion) {
    return null;
}
}

