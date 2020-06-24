package holauser.lea.holauser.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import holauser.lea.holauser.GlobalVars
import holauser.lea.holauser.GlobalVars.CHANNEL_ID
import holauser.lea.holauser.R
import java.util.*

class PlayerService: Service() {

    companion object {
        const val BROADCAST_REIKI = "broadcast_reiki"
        const val REIKI_MESSAGE = "reiki_message"

        const val NOTIFY_ID = 1337
        const val FOREGROUND_ID = 1338
    }

    private val timer = Timer()
    private var rep: MediaPlayer? = null
    private var repMusic: MediaPlayer? = null
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var broadcaster: LocalBroadcastManager

    private fun sendResult(title: String, time: String) {
        val intent = Intent(BROADCAST_REIKI)
        intent.putExtra(REIKI_MESSAGE, title + "\n" + time)
        broadcaster.sendBroadcast(intent)
    }

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
        startForeground(FOREGROUND_ID, buildForegroundNotification())
    }

    private fun buildForegroundNotification(): Notification {
        val b = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        b.setOngoing(true)
                .setSubText(getString(R.string.app_name))
                .setSmallIcon(R.drawable.reiki)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.icon))
        return b.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onStartSession()
        return START_STICKY
    }

    private fun onStartSession() {
        val globalVariable = applicationContext as GlobalVars
        rep = MediaPlayer.create(this, globalVariable.sonido)
        val frequency = 1000 * globalVariable.tiempo * 60
        timer.purge()

        repMusic = if (globalVariable.musicToPlay == null) MediaPlayer.create(this, R.raw.relaxing1)
                    else MediaPlayer.create(this, globalVariable.musicToPlay)

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
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            stopForeground(true)
            timer.cancel()
            countDownTimer.cancel()
            (applicationContext as GlobalVars).isPlaying = false
            repMusic?.stop()
            repMusic?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? { return null }
}