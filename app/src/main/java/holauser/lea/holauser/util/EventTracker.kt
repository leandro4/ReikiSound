package holauser.lea.holauser.util

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import holauser.lea.holauser.R
import holauser.lea.holauser.ReikiSound

object EventTracker {

    private const val REIKI_SESSION = "reikiSession"
    private const val DURATION = "duration"
    private const val FREQUENCY = "frequency"

    private const val BELL = "bell"
    private const val CUENCO = "cuenco"
    private const val KOSHI = "koshi"
    private const val TRIANGLE = "triangle"

    private const val BKGND_MUSIC = "bkgnd_music"
    private const val NO_MUSIC = "no_music"
    private const val LOCAL_MUSIC = "local_music"
    private const val DEFAULT_MUSIC = "default_music"

    fun trackReikiSession(context: Context) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        val startTime = DataManager.getStartSessionTime(context)
        val endTime = System.currentTimeMillis()

        val duration = (endTime - startTime) / 1000

        if (duration < 60) return

        val bell = when (DataManager.getBell(context)) {
            R.raw.cuenco -> CUENCO
            R.raw.koshi -> KOSHI
            else -> TRIANGLE
        }

        val uri = ((context.applicationContext) as ReikiSound).musicToPlay
        val music = when (DataManager.isBackgroundMusicEnabled(context)) {
            false -> NO_MUSIC
            else -> uri?.let { LOCAL_MUSIC } ?: DEFAULT_MUSIC
        }

        val bundle = Bundle()
        bundle.putLong(DURATION, duration)
        bundle.putInt(FREQUENCY, DataManager.getFrequency(context))
        bundle.putString(BELL, bell)
        bundle.putString(BKGND_MUSIC, music)
        firebaseAnalytics.logEvent(REIKI_SESSION, bundle)
    }

}