package holauser.lea.holauser.util

import android.content.Context
import holauser.lea.holauser.R

object DataManager {

    private val PREFIX = "saved_prefs"

    private val FREQUENCY = "FREQUENCY"
    private val SOUND = "SOUND"
    private val VOLUME = "VOLUME"
    private val MODE_ON = "MODE_ON"
    private val BACKGOURND_MUSIC_ENABLED = "BACKGOURND_MUSIC_ENABLED"

    fun getFrequency(context: Context): Int {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(FREQUENCY, 3)
    }

    fun setFrequency(context: Context, frequency: Int) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(FREQUENCY, frequency)
    }

    fun getBell(context: Context): Int {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(SOUND, R.raw.cuenco)
    }

    fun setBell(context: Context, bell: Int) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(SOUND, bell)
    }

    fun getVolume(context: Context): Float {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(VOLUME, 7f)
    }

    fun setVolume(context: Context, volume: Float) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(VOLUME, volume)
    }

    fun isModeOn(context: Context): Boolean {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(MODE_ON, false)
    }

    fun setModeOn(context: Context, modeOn: Boolean) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(MODE_ON, modeOn)
    }

    fun isBackgroundMusicEnabled(context: Context): Boolean {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(BACKGOURND_MUSIC_ENABLED, false)
    }

    fun setBackgroundMusicEnabled(context: Context, enabled: Boolean) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(BACKGOURND_MUSIC_ENABLED, enabled)
    }
}