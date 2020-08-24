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
    private const val DEFAULT_MUSIC_2 = "default_music_2"
    private const val DEFAULT_MUSIC_3 = "default_music_3"
    private const val DEFAULT_MUSIC_4 = "default_music_4"
    private const val DEFAULT_MUSIC_5 = "default_music_5"
    private const val DEFAULT_MUSIC_6 = "default_music_6"
    private const val DEFAULT_MUSIC_7 = "default_music_7"

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

        val music = when (DataManager.getBackgroundMusicOption(context)) {
            Constants.LOCAL_MUSIC -> LOCAL_MUSIC
            Constants.DEFAULT_1 -> DEFAULT_MUSIC
            Constants.DEFAULT_2 -> DEFAULT_MUSIC_2
            Constants.DEFAULT_3 -> DEFAULT_MUSIC_3
            Constants.DEFAULT_4 -> DEFAULT_MUSIC_4
            Constants.DEFAULT_5 -> DEFAULT_MUSIC_5
            Constants.DEFAULT_6 -> DEFAULT_MUSIC_6
            Constants.DEFAULT_7 -> DEFAULT_MUSIC_7
            else -> NO_MUSIC
        }

        val bundle = Bundle()
        bundle.putLong(DURATION, duration)
        bundle.putInt(FREQUENCY, DataManager.getFrequency(context) / 60)
        bundle.putString(BELL, bell)
        bundle.putString(BKGND_MUSIC, music)
        firebaseAnalytics.logEvent(REIKI_SESSION, bundle)
    }

}