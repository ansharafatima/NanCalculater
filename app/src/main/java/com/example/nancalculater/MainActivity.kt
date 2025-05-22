package com.example.nancalculater

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private lateinit var drumPlayer: MediaPlayer
    private lateinit var guitarPlayer: MediaPlayer
    private lateinit var pianoPlayer: MediaPlayer
    private lateinit var cymbalsPlayer: MediaPlayer
    private lateinit var flutePlayer: MediaPlayer
    private lateinit var maracasPlayer: MediaPlayer

    private lateinit var mainLayout: ConstraintLayout

    private val selectedInstruments = mutableMapOf<ImageButton, MediaPlayer>()


    private fun showWowPopup() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_wow, null)

        val closeButton = dialogView.findViewById<Button>(R.id.closeButton)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        closeButton.setOnClickListener {
            dialog.dismiss()
            if(selectedInstruments.isNotEmpty()){
                selectedInstruments.forEach({(button, player) ->
                    button.alpha=1.0f
                    player.stop()
                })
            }
            selectedInstruments.clear()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    fun toggleInstrument(button: ImageButton, player: MediaPlayer) {
        if (selectedInstruments.containsKey(button)) {
            selectedInstruments.remove(button)
            button.alpha = 1.0f  // Button not selected (full opacity)
        } else {
            selectedInstruments[button] = player
            button.alpha = 0.5f  // Button selected (half-transparent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drumButton = findViewById<ImageButton>(R.id.buttonDrum)
        val guitarButton = findViewById<ImageButton>(R.id.buttonGuitar)
        val pianoButton = findViewById<ImageButton>(R.id.buttonPiano)
        val cymbalsButton = findViewById<ImageButton>(R.id.buttonCymbals)
        val fluteButton = findViewById<ImageButton>(R.id.buttonFlute)
        val maracasButton = findViewById<ImageButton>(R.id.buttonMaracas)
        val mixButton = findViewById<Button>(R.id.buttonMix)
        val infoButton = findViewById<ImageButton>(R.id.buttonInfo)
        val volumeSeekBar = findViewById<SeekBar>(R.id.volumeSeekBar)
        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        mainLayout = findViewById(R.id.mainLayout)

        // Bounce Animation
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)

        // Load sounds
        drumPlayer = MediaPlayer.create(this, R.raw.drum)
        guitarPlayer = MediaPlayer.create(this, R.raw.guitar)
        pianoPlayer = MediaPlayer.create(this, R.raw.piano)
        cymbalsPlayer = MediaPlayer.create(this, R.raw.cymbals)
        flutePlayer = MediaPlayer.create(this, R.raw.flute)
        maracasPlayer = MediaPlayer.create(this, R.raw.maracas)

        fun playSound(player: MediaPlayer, button: ImageButton) {
            button.startAnimation(bounceAnim)
            player.start()
        }

        drumButton.setOnClickListener { toggleInstrument(drumButton, drumPlayer) }
        guitarButton.setOnClickListener { toggleInstrument(guitarButton, guitarPlayer) }
        pianoButton.setOnClickListener { toggleInstrument(pianoButton, pianoPlayer) }
        cymbalsButton.setOnClickListener { toggleInstrument(cymbalsButton, cymbalsPlayer) }
        fluteButton.setOnClickListener { toggleInstrument(fluteButton, flutePlayer) }
        maracasButton.setOnClickListener { toggleInstrument(maracasButton, maracasPlayer) }

      mixButton.setOnClickListener {
           drumButton.startAnimation(bounceAnim)
          guitarButton.startAnimation(bounceAnim)
            pianoButton.startAnimation(bounceAnim)
           cymbalsButton.startAnimation(bounceAnim)
            fluteButton.startAnimation(bounceAnim)
            maracasButton.startAnimation(bounceAnim)

           drumPlayer.start()
           guitarPlayer.start()
            pianoPlayer.start()
           cymbalsPlayer.start()
           flutePlayer.start()
           maracasPlayer.start()

            showWowPopup()
       }

        mixButton.setOnClickListener {
            if (selectedInstruments.isNotEmpty()) {
                selectedInstruments.forEach { (button, player) ->
                    button.startAnimation(bounceAnim)
                    player.start()
                }
                showWowPopup()
            } else {
                Toast.makeText(this, "Please select at least one instrument! 🎵", Toast.LENGTH_SHORT).show()
            }
        }

        infoButton.setOnClickListener { showInfoDialog() }

        // Volume control
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                listOf(drumPlayer, guitarPlayer, pianoPlayer, cymbalsPlayer, flutePlayer, maracasPlayer).forEach {
                    it.setVolume(volume, volume)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Dark mode toggle
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mainLayout.setBackgroundColor(resources.getColor(android.R.color.black))
            } else {
                mainLayout.setBackgroundResource(R.drawable.musicbg)
            }
        }
    }

    private fun showInfoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)

        val closeButton = dialogView.findViewById<Button>(R.id.closeButton)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        listOf(drumPlayer, guitarPlayer, pianoPlayer, cymbalsPlayer, flutePlayer, maracasPlayer).forEach {
            it.release()
        }
    }
}
