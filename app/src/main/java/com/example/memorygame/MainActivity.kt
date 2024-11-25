package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        val avatarButton = findViewById<ImageButton>(R.id.avatarButton)

        // iniciamos el mediaplayer con la cancion
        mediaPlayer = MediaPlayer.create(this, R.raw.musica)

        val intent = Intent(this, StartGameActivity::class.java)

        avatarButton.setOnClickListener{
            val musicServiceIntent = Intent(this, MusicService::class.java)
            startService(musicServiceIntent)

            mediaPlayer?.apply {
                if (!isPlaying) {
                    // canción en bucle.
                    isLooping = true
                    start()
                }
            }

            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        // soundPool?.release()
    }

}