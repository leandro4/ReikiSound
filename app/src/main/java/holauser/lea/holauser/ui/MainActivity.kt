package holauser.lea.holauser.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle

import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.View

import holauser.lea.holauser.GlobalVars
import holauser.lea.holauser.R
import holauser.lea.holauser.services.PlayerService
import holauser.lea.holauser.util.Animations
import holauser.lea.holauser.util.DataManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseReceiverActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        volumePicker.setMinValue(1)
        volumePicker.setMaxValue(10)
        volumePicker.value = 7

        cb_enable_music.setOnCheckedChangeListener { _, isChecked -> if (isChecked) showSelectAudioDialog() }

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val time = intent.getStringExtra(PlayerService.REIKI_MESSAGE)
                chronometer!!.text = time
            }
        }

        setStopMode(!(applicationContext as GlobalVars).isPlaying)

        btn_play.setOnClickListener { onPlayClick() }
        btnPage.setOnClickListener { onDonateClick() }

        numberPicker.value = DataManager.getFrequency(this)
    }

    private fun onPlayClick() {
        Animations.animateScale(btn_play)

        val gb = applicationContext as GlobalVars

        if (gb.isPlaying) {
            stopService(Intent(this@MainActivity, PlayerService::class.java))
            setStopMode(true)
        } else {
            gb.tiempo = numberPicker!!.value
            gb.volume = (volumePicker!!.value / 10.0).toFloat()
            setStopMode(false)
            DataManager.setFrequency(this, gb.tiempo)
            gb.isPlayMusic = cb_enable_music.isChecked
            startService(Intent(this, PlayerService::class.java))
        }
    }

    private fun setStopMode(stop: Boolean) {
        rl_content_select.visibility = if (stop) View.VISIBLE else View.GONE
        chronometer!!.visibility = if (!stop) View.VISIBLE else View.GONE
        root.setBackgroundColor(ContextCompat.getColor(this, if (stop) R.color.white else R.color.dark_screen))
        btn_play.setImageResource(if (stop) R.drawable.ic_play else R.drawable.ic_stop)
    }

    private fun onDonateClick() {
        Animations.animateScale(btnPage)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setStopMode(!(applicationContext as GlobalVars).isPlaying)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (MediaPlayer.create(this, uri) == null) {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.selected_audio)).setMessage(getString(R.string.selected_audio_body))
                        .setPositiveButton("Ok") { _, _ -> selectAudio() }
                        .show()
            } else {
                (applicationContext as GlobalVars).musicToPlay = uri
            }
        } else if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            cb_enable_music.isChecked = false
        }
    }

    companion object {
        const val AUDIO_SELECTION = 1000
    }
}
