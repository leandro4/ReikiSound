package holauser.lea.holauser.ui

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle

import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.view.WindowManager

import holauser.lea.holauser.GlobalVars
import holauser.lea.holauser.R
import holauser.lea.holauser.services.SoundService
import holauser.lea.holauser.util.Animations
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var receiver: BroadcastReceiver
    private var link = "https://sites.google.com/view/reikisound"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccentExtraLigth)
        }

        volumePicker.setMinValue(1)
        volumePicker.setMaxValue(10)
        volumePicker.value = 10

        cb_enable_music.setOnCheckedChangeListener { _, isChecked -> if (isChecked) showSelectAudioDialog() }

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val time = intent.getStringExtra(SoundService.REIKI_MESSAGE)
                chronometer!!.text = time
            }
        }

        setStopMode(!(applicationContext as GlobalVars).isPlaying)

        btn_play.setOnClickListener { onPlayClick() }
        btnPage.setOnClickListener { onDonateClick() }
    }

    private fun onPlayClick() {
        Animations.animateScale(btn_play)

        val gb = applicationContext as GlobalVars

        if (gb.isPlaying) {
            stopService(Intent(this@MainActivity, SoundService::class.java))
            setStopMode(true)
        } else {
            gb.tiempo = numberPicker!!.value
            gb.volume = (volumePicker!!.value / 10.0).toFloat()
            setStopMode(false)

            gb.isPlayMusic = cb_enable_music.isChecked

            registerBroadcastReceiver(true)
            startService(Intent(this@MainActivity, SoundService::class.java))
        }
    }

    private fun setStopMode(stop: Boolean) {
        rl_content_select.visibility = if (stop) View.VISIBLE else View.GONE
        chronometer!!.visibility = if (!stop) View.VISIBLE else View.GONE
        btn_play.setImageResource(if (stop) R.drawable.ic_play else R.drawable.ic_stop)
    }

    private fun onDonateClick() {
        Animations.animateScale(btnPage)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    public override fun onStop() {
        super.onStop()
        registerBroadcastReceiver(false)
    }

    public override fun onResume() {
        super.onResume()
        registerBroadcastReceiver(true)
        setStopMode(!(applicationContext as GlobalVars).isPlaying)
    }

    private fun registerBroadcastReceiver(register: Boolean) {
        if (register) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(SoundService.BROADCAST_REIKI))
        } else
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_OK) {

            val uri = data.data
            if (MediaPlayer.create(this, uri) == null) {

                val title = getString(R.string.selected_audio)
                val message = getString(R.string.selected_audio_body)

                val builder = AlertDialog.Builder(this)
                builder.setTitle(title)
                builder.setMessage(message)
                builder.setPositiveButton("Ok") { _, _ -> selectAudio() }
                builder.show()
            } else {
                val gb = applicationContext as GlobalVars
                gb.musicToPlay = uri
            }
        } else if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            cb_enable_music.isChecked = false
        }
    }

    private fun showSelectAudioDialog() {
        val title = getString(R.string.select_audio_title)
        val message = getString(R.string.select_audio_body)
        val positiveBtn = getString(R.string.select_audio_select)
        val defaultBtn = getString(R.string.select_audio_default)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveBtn) { _, _ -> selectAudio() }
        builder.setNegativeButton(defaultBtn) { _, _ ->
            val gb = applicationContext as GlobalVars
            gb.musicToPlay = null
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun selectAudio() {
        val intentUpload = Intent()
        intentUpload.type = "audio/*"
        intentUpload.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intentUpload, AUDIO_SELECTION)
    }

    companion object {
        const val AUDIO_SELECTION = 1000
    }
}
