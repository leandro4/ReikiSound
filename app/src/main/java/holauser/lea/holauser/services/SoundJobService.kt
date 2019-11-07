package holauser.lea.holauser.services

import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import holauser.lea.holauser.GlobalVars
import holauser.lea.holauser.R
import java.util.*

class SoundJobService: JobIntentService() {

    private val timer = Timer()
    private lateinit var rep: MediaPlayer
    private lateinit var repMusic: MediaPlayer
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var broadcaster: LocalBroadcastManager

    companion object {
        const val BROADCAST_REIKI = "broadcast_reiki"
        const val REIKI_MESSAGE = "reiki_message"
        const val JOB_ID = 999
    }

    fun sendResult(title: String, time: String) {
        val intent = Intent(BROADCAST_REIKI)
        intent.putExtra(REIKI_MESSAGE, title + "\n" + time)
        broadcaster.sendBroadcast(intent)

        //makeNotification(this, time)
    }

    override fun onHandleWork(intent: Intent) {
        broadcaster = LocalBroadcastManager.getInstance(this)

        val globalVariable = applicationContext as GlobalVars
        rep = MediaPlayer.create(this, globalVariable.sonido)
        val frequency = 1000 * globalVariable.tiempo * 60
        timer.purge()

        repMusic = if (globalVariable.musicToPlay == null) MediaPlayer.create(this, R.raw.relaxing1)
                    else MediaPlayer.create(this, globalVariable.musicToPlay)

        if (globalVariable.isPlayMusic) {
            repMusic.isLooping = true
            repMusic.setVolume(1f, 1f)
            repMusic.start()
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

        rep.setVolume(globalVariable.volume, globalVariable.volume)

        val timerTask = object : TimerTask() {
            override fun run() {
                rep.start()
                globalVariable.isPlaying = true

            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, frequency.toLong())
    }

    override fun onDestroy() {
        timer.cancel()
        countDownTimer.cancel()
        (applicationContext as GlobalVars).isPlaying = false
        try {
            repMusic.stop()
            repMusic.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
}