package holauser.lea.holauser;
import android.app.Application;

public class GlobalVars extends Application {
    private int audio = R.raw.cuenco;
    private int tiempo = 3;
    private boolean isPlaying = false;

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
}
