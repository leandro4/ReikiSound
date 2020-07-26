package holauser.lea.holauser.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build.VERSION.SDK
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import android.view.View
import android.view.View.*
import android.view.Window.FEATURE_ACTION_BAR_OVERLAY
import android.widget.AdapterView

import holauser.lea.holauser.ReikiSound
import holauser.lea.holauser.services.PlayerService
import holauser.lea.holauser.util.DataManager
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import android.widget.SeekBar
import holauser.lea.holauser.R
import androidx.constraintlayout.widget.ConstraintSet
import holauser.lea.holauser.util.EventTracker
import android.graphics.drawable.TransitionDrawable
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_splash.*

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
        initDarkModeControls()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or SYSTEM_UI_FLAG_LAYOUT_STABLE
                or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or SYSTEM_UI_FLAG_FULLSCREEN)
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
                flVolumeDialog.visibility = VISIBLE
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                flVolumeDialog.visibility = INVISIBLE
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

    private fun initDarkModeControls() {
        ivDarkModeOn.setOnClickListener {
            if (it.isSelected) return@setOnClickListener
            DataManager.setDarkMode(this, true)
            updateDarkModeUI(true)
        }

        ivDarkModeOff.setOnClickListener {
            if (it.isSelected) return@setOnClickListener
            DataManager.setDarkMode(this, false)
            updateDarkModeUI(false)
        }

    }

    private fun updateDarkModeUI(darkModeOn: Boolean) {
        if (darkModeOn) {
            imageView.setImageResource(R.drawable.ic_logo_small_dark)

            if (SDK_INT < 21)
                root.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            else {
                val anim = ValueAnimator.ofArgb(ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.black))
                anim.addUpdateListener { valueAnimator -> root.setBackgroundColor(valueAnimator.animatedValue as Int) }
                anim.duration = 200
                anim.start()
            }

            ivDarkModeOn.setBackgroundResource(R.drawable.rectangle_rounded_gray)
            ivDarkModeOff.setBackgroundResource(R.drawable.rectangle_rounded_gray)
            btnPlay.setImageResource(R.drawable.ic_pause_dark)
            btnPlay.setBackgroundResource(R.drawable.btn_play_selected_dark)
        } else {
            imageView.setImageResource(R.drawable.ic_logo_small_disabled)

            if (SDK_INT < 21)
                root.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            else {
                val anim = ValueAnimator.ofArgb(ContextCompat.getColor(this, R.color.black), ContextCompat.getColor(this, R.color.white))
                anim.addUpdateListener { valueAnimator -> root.setBackgroundColor(valueAnimator.animatedValue as Int) }
                anim.duration = 200
                anim.start()
            }

            ivDarkModeOn.setBackgroundResource(R.drawable.light_mode_state_selector)
            ivDarkModeOff.setBackgroundResource(R.drawable.light_mode_state_selector)
            btnPlay.setImageResource(R.drawable.ic_pause)
            btnPlay.setBackgroundResource(R.drawable.btn_play_state)
        }
        ivDarkModeOn.isSelected = darkModeOn
        ivDarkModeOff.isSelected = !darkModeOn
    }

    private fun onPlayClick() {
        if (DataManager.isModeOn(this)) {
            EventTracker.trackReikiSession(this)
            setModeOnUI(false)
            stopService(Intent(this@MainActivity, PlayerService::class.java))
        } else {
            setModeOnUI(true)
            DataManager.setFrequency(this, numberPicker!!.value)
            startService(Intent(this, PlayerService::class.java))
            DataManager.saveStartSessionTime(this)
        }
        DataManager.setModeOn(this, !DataManager.isModeOn(this))
    }

    private fun setModeOnUI(modeOn: Boolean) {
        rl_content_select.visibility = if (modeOn) GONE else VISIBLE
        llChronometer!!.visibility = if (modeOn) VISIBLE else GONE
        btnPlay.isSelected = modeOn

        if (modeOn) {
            if (DataManager.isDarkModeOn(this)) {
                updateDarkModeUI(true)
            } else {
                imageView.setImageResource(R.drawable.ic_logo_small_disabled)
                btnPlay.setImageResource(R.drawable.ic_pause)
                btnPlay.setBackgroundResource(R.drawable.btn_play_state)
            }
            hideControls()
            root.setOnClickListener { showControls() }
        } else {
            if (DataManager.isDarkModeOn(this)) {
                updateDarkModeUI(false)
            }
            imageView.setImageResource(R.drawable.ic_logo_small)
            btnPlay.setImageResource(R.drawable.ic_play)
            btnPlay.setBackgroundResource(R.drawable.btn_play_state)
            root.setOnClickListener(null)
            runnable?.let { handler.removeCallbacks(it) }
        }
    }

    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private fun hideControls() {
        runnable?.let { handler.removeCallbacks(it) }
        runnable = Runnable {
            imageView.animate().alpha(0f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    imageView.visibility = INVISIBLE
                }
            })
            llDarkModeControls.animate().alpha(0f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    llDarkModeControls.visibility = INVISIBLE
                }
            })
            btnHelp.animate().alpha(0f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    btnHelp.visibility = INVISIBLE
                }
            })
            btnPlay.animate().alpha(0f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    btnPlay.visibility = INVISIBLE
                }
            })
        }
        handler.postDelayed(runnable!!, 2000)
    }

    private fun showControls() {
        if (btnPlay.visibility == INVISIBLE) {
            imageView.visibility = VISIBLE
            imageView.animate().alpha(1f).setDuration(200).setListener(null)
            llDarkModeControls.visibility = VISIBLE
            llDarkModeControls.animate().alpha(1f).setDuration(200).setListener(null)
            btnHelp.visibility = VISIBLE
            btnHelp.animate().alpha(1f).setDuration(200).setListener(null)
            btnPlay.visibility = VISIBLE
            btnPlay.animate().alpha(1f).setDuration(200).setListener(null)
        }
        hideControls()
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
