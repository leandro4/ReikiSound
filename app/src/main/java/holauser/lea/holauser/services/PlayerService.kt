package holauser.lea.holauser.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import holauser.lea.holauser.GlobalVars
import holauser.lea.holauser.R
import java.util.*

class PlayerService: Service() {

    companion object {
        const val BROADCAST_REIKI = "broadcast_reiki"
        const val REIKI_MESSAGE = "reiki_message"
        const val JOB_ID = 999
    }

    private val timer = Timer()
    private var rep: MediaPlayer? = null
    private var repMusic: MediaPlayer? = null
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var broadcaster: LocalBroadcastManager

    fun sendResult(title: String, time: String) {
        val intent = Intent(SoundJobService.BROADCAST_REIKI)
        intent.putExtra(SoundJobService.REIKI_MESSAGE, title + "\n" + time)
        broadcaster.sendBroadcast(intent)
    }

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, id: Int): Int {
        val globalVariable = applicationContext as GlobalVars
        rep = MediaPlayer.create(this, globalVariable.sonido)
        val frequency = 1000 * globalVariable.tiempo * 60
        timer.purge()

        if (globalVariable.musicToPlay == null)
            repMusic = MediaPlayer.create(this, R.raw.relaxing1)
        else
            repMusic = MediaPlayer.create(this, globalVariable.musicToPlay)

        if (globalVariable.isPlayMusic) {
            repMusic?.isLooping = true
            repMusic?.setVolume(1f, 1f)
            repMusic?.start()
        }

        val remaining = getString(R.string.remaining)

        countDownTimer = object : CountDownTimer(frequency.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                var min = (time / 60).toString()
                var seg = (time % 60).toString()
                if (min.length == 1) min = "0$min"
                if (seg.length == 1) seg = "0$seg"
                sendResult(remaining, String.format("%s:%s", min, seg))
            }

            override fun onFinish() {
                countDownTimer.start()
            }
        }
        countDownTimer.start()

        rep?.setVolume(globalVariable.volume, globalVariable.volume)

        val timerTask = object : TimerTask() {
            override fun run() {
                rep?.start()
                globalVariable.isPlaying = true

            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, frequency.toLong())

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        countDownTimer.cancel()
        (applicationContext as GlobalVars).isPlaying = false
        repMusic?.stop()
        repMusic?.release()
    }

    override fun onBind(intent: Intent?): IBinder? { return null }
}