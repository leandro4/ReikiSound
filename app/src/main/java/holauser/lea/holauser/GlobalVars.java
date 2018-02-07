package holauser.lea.holauser;
import android.app.Application;
import android.net.Uri;

public class GlobalVars extends Application {
    private int audio = R.raw.cuenco;
    private int tiempo = 1;
    private boolean isPlaying = false;
    private boolean playMusic = false;
    private Uri musicToPlay = null;

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
}
