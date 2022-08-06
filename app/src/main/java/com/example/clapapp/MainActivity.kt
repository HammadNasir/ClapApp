package com.example.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    // ? safe call operator - used for null references
    private var player:MediaPlayer? = null
    private lateinit var seekBar: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playBtn = findViewById<FloatingActionButton>(R.id.fabPlay)
        val pauseBtn = findViewById<FloatingActionButton>(R.id.fabPause)
        val stopBtn = findViewById<FloatingActionButton>(R.id.fabStop)

        seekBar = findViewById(R.id.seekBar)
        handler = Handler(Looper.getMainLooper())

        playBtn.setOnClickListener {
            if (player == null) {
                player = MediaPlayer.create(this, R.raw.claps)
                initializeSeekBar()
            }
            player?.start()
        }

        pauseBtn.setOnClickListener {
            player?.pause()
        }

        stopBtn.setOnClickListener {
            player?.stop()
            player?.reset()
            player?.release()
            player = null
            handler.removeCallbacks(runnable)
            seekBar.progress = 0
        }
    }

    private fun initializeSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) player?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)
        seekBar.max = player!!.duration // !! - not null assertion operator
        runnable = Runnable {
            seekBar.progress = player!!.currentPosition
            handler.postDelayed(runnable, 1000)

            val playedTime = player!!.currentPosition/1000
//            tvPlayed.text = "$playedTime:00"

            val duration = player!!.duration/1000

            val dueTime = duration - playedTime
//            tvDue.text = "$dueTime:00"
        }
        handler.postDelayed(runnable, 1000)
    }

}