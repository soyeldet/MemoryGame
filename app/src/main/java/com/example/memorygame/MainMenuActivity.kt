package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val intent = Intent(this, AvatarsActivity::class.java)
        val intent2 = Intent(this, AdminPanelActivity::class.java)
        val playerButton = findViewById<Button>(R.id.playerButton)
        val adminButton = findViewById<Button>(R.id.adminButton)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        mediaPlayer = MediaPlayer.create(this, R.raw.music)

        val musicServiceIntent = Intent(this, MusicService::class.java)
        musicServiceIntent.action = "START_MUSIC"
        startService(musicServiceIntent)

        playerButton.setOnClickListener{
            startActivity(intent)
        }

        adminButton.setOnClickListener{
            startActivity(intent2)
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}