package holauser.lea.holauser;
import android.app.Application;

public class GlobalVars extends Application {
    private int audio;
    private int tiempo;

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
}
