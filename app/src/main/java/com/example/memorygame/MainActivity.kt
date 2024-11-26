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

        avatarButton.setOnClickListener {
            // Iniciar el servicio para reproducir música
            val musicServiceIntent = Intent(this, MusicService::class.java)
            musicServiceIntent.action = "START_MUSIC"  // Acción para iniciar la música
            startService(musicServiceIntent)  // Inicia el servicio y la música

            // Inicia la siguiente actividad
            startActivity(intent)
        }

    }


}