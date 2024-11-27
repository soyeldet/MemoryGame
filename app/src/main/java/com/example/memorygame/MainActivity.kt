package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)

        // Establece el layout de la actividad.
        setContentView(R.layout.activity_main)

        // Configura la interfaz para ocultar la barra de estado y navegación en modo pantalla completa e inmersiva.
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Crea un Intent para navegar a la actividad MainMenuActivity.
        val intent = Intent(this, MainMenuActivity::class.java)

        // Inicia un retraso de 800 ms antes de iniciar la actividad.
        Handler(Looper.getMainLooper()).postDelayed({
            // Inicia la actividad MainMenuActivity después del retraso.
            startActivity(intent)
        }, 800)
    }
}
