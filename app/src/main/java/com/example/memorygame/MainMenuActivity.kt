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

        // Crea los Intents para navegar a las actividades correspondientes.
        val intent = Intent(this, AvatarsActivity::class.java)
        val intent2 = Intent(this, AdminPanelActivity::class.java)

        // Encuentra los botones por su ID en el layout.
        val playerButton = findViewById<Button>(R.id.playerButton)
        val adminButton = findViewById<Button>(R.id.adminButton)

        // Configura la interfaz para ocultar la barra de estado y navegación en modo pantalla completa e inmersiva.
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Crea el objeto MediaPlayer y reproduce la música de fondo.
        mediaPlayer = MediaPlayer.create(this, R.raw.music)

        // Crea un Intent para iniciar el servicio de música.
        val musicServiceIntent = Intent(this, MusicService::class.java)
        musicServiceIntent.action = "START_MUSIC"
        startService(musicServiceIntent)

        // Define el comportamiento del botón de "player" para iniciar la actividad AvatarsActivity.
        playerButton.setOnClickListener {
            startActivity(intent)
        }

        // Define el comportamiento del botón de "admin" para iniciar la actividad AdminPanelActivity.
        adminButton.setOnClickListener {
            startActivity(intent2)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Evita que el usuario regrese a la actividad anterior.
    }
}
