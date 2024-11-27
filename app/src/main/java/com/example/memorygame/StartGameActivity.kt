package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartGameActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)


        val intent = Intent(this, AvatarsActivity::class.java)
        val intent2 = Intent(this, AdminPanelActivity::class.java)
        val musicButton = findViewById<Button>(R.id.musicButton)
        val playerButton = findViewById<Button>(R.id.playerButton)

        // Musica
        var isMusicPlaying = false
        val adminButton = findViewById<Button>(R.id.adminButton)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        playerButton.setOnClickListener{
            startActivity(intent)
        }

        adminButton.setOnClickListener{
            startActivity(intent2)
        }

        // Config boton musica
        musicButton.setOnClickListener {
            val musicServiceIntent = Intent(this, MusicService::class.java)

            if (isMusicPlaying) {
                // Si la música está sonando, la detenemos
                musicServiceIntent.action = "STOP_MUSIC"
                startService(musicServiceIntent)
                isMusicPlaying = false  // Actualizamos el estado de la música
            } else {
                // Si la música no está sonando, la iniciamos
                musicServiceIntent.action = "START_MUSIC"
                startService(musicServiceIntent)
                isMusicPlaying = true  // Actualizamos el estado de la música
            }
        }


        //
    }
}