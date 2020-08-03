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
    private val BACKGOURND_MUSIC_OPTION = "BACKGOURND_MUSIC_OPTION"
    private val STARTING_SESSION_TIME = "STARTING_SESSION_TIME"
    private val DARK_MODE_ON = "DARK_MODE_ON"

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

    fun getVolume(context: Context): Int {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(VOLUME, 50)
    }

    fun setVolume(context: Context, volume: Int) {
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

    fun getBackgroundMusicOption(context: Context): Int {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(BACKGOURND_MUSIC_OPTION, R.raw.relaxing1)
    }

    fun setBackgroundMusicOption(context: Context, option: Int) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(BACKGOURND_MUSIC_OPTION, option)
    }

    fun isDarkModeOn(context: Context): Boolean {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(DARK_MODE_ON, true)
    }

    fun setDarkMode(context: Context, enabled: Boolean) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(DARK_MODE_ON, enabled)
    }

    fun saveStartSessionTime(context: Context) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(STARTING_SESSION_TIME, System.currentTimeMillis())
    }

    fun getStartSessionTime(context: Context): Long {
        val shared = SharedPreferencesEditor(context, PREFIX)
        return shared.valueForKey(STARTING_SESSION_TIME, System.currentTimeMillis())
    }
}