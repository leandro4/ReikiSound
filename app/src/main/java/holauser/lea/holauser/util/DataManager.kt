package holauser.lea.holauser.util

import android.content.Context
import holauser.lea.holauser.R

object DataManager {

    private val PREFIX = "saved_prefs"
    private val FREQUENCY = "FREQUENCY"
    private val BELL = "BELL"

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
        return shared.valueForKey(BELL, R.raw.cuenco)
    }

    fun setBell(context: Context, bell: Int) {
        val shared = SharedPreferencesEditor(context, PREFIX)
        shared.setValueForKey(BELL, bell)
    }
}