package holauser.lea.holauser.ui.activities

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import holauser.lea.holauser.ReikiSound
import holauser.lea.holauser.R
import holauser.lea.holauser.services.PlayerService
import holauser.lea.holauser.ui.activities.MainActivity

abstract class BaseReceiverActivity: AppCompatActivity() {

    protected lateinit var receiver: BroadcastReceiver
    protected val link = "https://sites.google.com/view/reikisound"

    public override fun onStop() {
        super.onStop()
        registerBroadcastReceiver(false)
    }

    public override fun onResume() {
        super.onResume()
        registerBroadcastReceiver(true)
    }

    private fun registerBroadcastReceiver(register: Boolean) {
        if (register) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(PlayerService.BROADCAST_REIKI))
        } else
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    protected fun selectAudio() {
        val intentUpload = Intent()
        intentUpload.type = "audio/*"
        intentUpload.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intentUpload, MainActivity.AUDIO_SELECTION)
    }
}