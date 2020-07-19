package holauser.lea.holauser.ui.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.AdapterView

import holauser.lea.holauser.ReikiSound
import holauser.lea.holauser.services.PlayerService
import holauser.lea.holauser.util.DataManager
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import android.widget.SeekBar
import holauser.lea.holauser.R
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet


class MainActivity : BaseReceiverActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val time = intent.getStringExtra(PlayerService.REIKI_MESSAGE)
                chronometer!!.text = time
            }
        }

        btnPlay.setOnClickListener { onPlayClick() }
        btnHelp.setOnClickListener { onDonateClick() }

        numberPicker.value = DataManager.getFrequency(this)

        initSpinner()
        initSeekBarVol()
    }

    private fun initSeekBarVol() {
        sbVolume.progress = DataManager.getVolume(this)
        sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                p0?.let {
                    if (progress < 10) it.progress = 10
                    tvVolume.text = String.format("%d%%", it.progress)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(volumeContainer)
                    val biasedValue = it.progress / 100.0f
                    constraintSet.setHorizontalBias(R.id.flVolumeDialog, biasedValue)
                    constraintSet.applyTo(volumeContainer)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                flVolumeDialog.visibility = View.VISIBLE
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                flVolumeDialog.visibility = View.INVISIBLE
                DataManager.setVolume(this@MainActivity, p0?.progress ?: 50)
            }
        })
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter<String>(this, R.layout.adapter_music_source_row, R.id.tvOption, resources.getStringArray(R.array.music_source))
        spMusicSource.adapter = adapter
        spMusicSource.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                DataManager.setBackgroundMusicEnabled(this@MainActivity, p2 != 0)
                when (p2) {
                    1 -> selectAudio()
                    2 -> (applicationContext as ReikiSound).musicToPlay = null
                    else -> {}
                }
            }
        }
    }

    private fun onPlayClick() {
        if (DataManager.isModeOn(this)) {
            setModeOnUI(false)
            stopService(Intent(this@MainActivity, PlayerService::class.java))
        } else {
            setModeOnUI(true)
            DataManager.setFrequency(this, numberPicker!!.value)
            startService(Intent(this, PlayerService::class.java))
        }
        DataManager.setModeOn(this, !DataManager.isModeOn(this))
    }

    private fun setModeOnUI(modeOn: Boolean) {
        rl_content_select.visibility = if (modeOn) View.GONE else View.VISIBLE
        chronometer!!.visibility = if (modeOn) View.VISIBLE else View.GONE
        //root.setBackgroundColor(ContextCompat.getColor(this, if (stop) R.color.white else R.color.dark_screen))
        btnPlay.setImageResource(if (modeOn) R.drawable.ic_pause else R.drawable.ic_play)
        btnPlay.isSelected = modeOn
        imageView.setImageResource(if (modeOn) R.drawable.ic_logo_small_disabled else R.drawable.ic_logo_small)
    }

    private fun onDonateClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        setModeOnUI(DataManager.isModeOn(this))
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
                (applicationContext as ReikiSound).musicToPlay = uri
            }
        } else if (requestCode == AUDIO_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            spMusicSource.setSelection(0)
        }
    }

    companion object {
        const val AUDIO_SELECTION = 1000
    }
}
