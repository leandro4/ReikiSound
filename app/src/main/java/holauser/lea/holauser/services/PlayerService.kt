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
import holauser.lea.holauser.ReikiSound
import holauser.lea.holauser.ReikiSound.CHANNEL_ID
import holauser.lea.holauser.R
import holauser.lea.holauser.util.Constants
import holauser.lea.holauser.util.DataManager
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
    private var countDownTimer: CountDownTimer? = null
    private lateinit var broadcaster: LocalBroadcastManager

    private fun sendResult(time: String) {
        val intent = Intent(BROADCAST_REIKI)
        intent.putExtra(REIKI_MESSAGE, time)
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
                .setSubText(getString(R.string.app_name) + " playing")
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_logo))
        return b.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onStartSession()
        return START_STICKY
    }

    private fun onStartSession() {
        val globalVariable = applicationContext as ReikiSound
        rep = MediaPlayer.create(this, DataManager.getBell(this))
        val frequency = 1000 * DataManager.getFrequency(this) * 60
        timer.purge()

        repMusic = when (DataManager.getBackgroundMusicOption(this)) {
            Constants.LOCAL_MUSIC -> MediaPlayer.create(this, globalVariable.musicToPlay)
            Constants.DEFAULT_1 -> MediaPlayer.create(this, R.raw.relaxing1)
            Constants.DEFAULT_2 -> MediaPlayer.create(this, R.raw.relaxing2)
            Constants.DEFAULT_3 -> MediaPlayer.create(this, R.raw.relaxing3)
            Constants.DEFAULT_4 -> MediaPlayer.create(this, R.raw.relaxing4)
            Constants.DEFAULT_5 -> MediaPlayer.create(this, R.raw.relaxing5)
            Constants.DEFAULT_6 -> MediaPlayer.create(this, R.raw.relaxing6)
            else -> null
        }

        if (DataManager.isBackgroundMusicEnabled(this)) {
            repMusic?.isLooping = true
            repMusic?.setVolume(1f, 1f)
            repMusic?.start()
        }

        countDownTimer = object : CountDownTimer(frequency.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                var min = (time / 60).toString()
                var seg = (time % 60).toString()
                if (min.length == 1) min = "0$min"
                if (seg.length == 1) seg = "0$seg"
                sendResult(String.format("%s:%s", min, seg))
            }

            override fun onFinish() {
                countDownTimer?.start()
            }
        }
        countDownTimer?.start()

        val vol = DataManager.getVolume(this)/100.0f
        rep?.setVolume(vol, vol)

        val timerTask = object : TimerTask() {
            override fun run() {
                rep?.start()
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, frequency.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            stopForeground(true)
            timer.cancel()
            countDownTimer?.cancel()
            DataManager.setModeOn(this, false)
            repMusic?.stop()
            repMusic?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? { return null }
}