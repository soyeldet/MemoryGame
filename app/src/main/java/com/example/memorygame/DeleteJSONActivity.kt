package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class DeleteJSONActivity : AppCompatActivity() {
    private lateinit var yesButton: Button  // Botón para confirmar la eliminación de los datos
    private lateinit var noButton: Button   // Botón para cancelar la eliminación y regresar

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE  // Establece la orientación de la pantalla en horizontal
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_json)  // Asocia el layout con la actividad

        // Configura la actividad para pantalla completa, ocultando barras de estado y navegación
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Inicializa los botones
        yesButton = findViewById(R.id.yesButton)  // Botón de "Sí", para confirmar la eliminación
        noButton = findViewById(R.id.noButton)    // Botón de "No", para cancelar la acción

        // Configura el comportamiento del botón "No" para regresar al panel de administración
        noButton.setOnClickListener {
            val intent = Intent(this, AdminPanelActivity::class.java)  // Inicia el panel de administración
            startActivity(intent)
        }

        // Configura el comportamiento del botón "Sí" para eliminar el archivo de datos
        yesButton.setOnClickListener {
            val file = File(this.filesDir, "player_data.json")  // Accede al archivo JSON que contiene los datos del jugador
            file.delete()  // Elimina el archivo de datos
            val intent = Intent(this, AdminPanelActivity::class.java)  // Vuelve al panel de administración
            startActivity(intent)
            Toast.makeText(this, "Grupos eliminados correctamente", Toast.LENGTH_SHORT).show()  // Muestra un mensaje de éxito
        }
    }
}
