package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class GroupActivity : AppCompatActivity() {

    private lateinit var groupsJSON: JSONObject  // Objeto JSON que contiene los grupos de jugadores
    private var group: Int? = null  // Identificador del grupo que se seleccionó
    private lateinit var backButton: ImageButton  // Botón para regresar al panel de administración

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE  // Establece la orientación de la actividad a horizontal
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)  // Asocia el layout con la actividad

        // Configura la actividad para pantalla completa, ocultando barras de estado y navegación
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Inicializa el botón de regreso
        backButton = findViewById(R.id.backButton)

        // Obtiene el número del grupo desde los datos enviados con el Intent
        group = intent.getIntExtra("group", -1)  // Si no se pasa un grupo, se asigna -1
        if (group == -1) {
            Toast.makeText(this, "Grupo no encontrado", Toast.LENGTH_SHORT).show()  // Si el grupo no se encuentra, muestra un mensaje
            return  // Sale de la actividad si no se encuentra el grupo
        }

        // Obtiene los datos de los grupos desde el archivo JSON
        groupsJSON = getGroups()

        // Carga los datos del grupo seleccionado
        loadGroupData(group!!)

        // Configura el comportamiento del botón de regresar
        backButton.setOnClickListener {
            val intent = Intent(this, AdminPanelActivity::class.java)  // Regresa al panel de administración
            startActivity(intent)
        }
    }

    // Función que lee los grupos del archivo JSON y devuelve un objeto JSONObject
    private fun getGroups(): JSONObject {
        val file = File(this.filesDir, "player_data.json")  // Accede al archivo JSON de los datos de los jugadores
        val playerData = JSONObject(file.readText())  // Lee el contenido del archivo y lo convierte en JSONObject
        return playerData
    }

    // Función que carga los datos de un grupo específico
    private fun loadGroupData(group: Int) {
        val groupsArray = groupsJSON.getJSONArray("groups")  // Obtiene el array de grupos desde el JSON

        // Recorre todos los grupos para encontrar el grupo seleccionado
        for (i in 0 until groupsArray.length()) {
            val groupObject = groupsArray.getJSONObject(i)
            if (groupObject.getInt("group") == group) {  // Si el grupo coincide con el seleccionado
                val avatars = groupObject.getJSONArray("avatars")  // Obtiene los avatares del grupo
                // Recorre los avatares y carga sus datos
                for (j in 0 until avatars.length()) {
                    val avatarObj = avatars.getJSONObject(j)
                    val avatar = avatarObj.getInt("avatar")  // Obtiene el avatar
                    val levels = avatarObj.getJSONArray("levels")  // Obtiene los niveles para ese avatar

                    // Llama a la función que establece los datos de cada avatar en la UI
                    when (avatar) {
                        1 -> {
                            setAvatarData(avatar, levels, R.id.avatar1, R.id.nivel1avatar1, R.id.nivel2avatar1, R.id.nivel3avatar1)
                        }
                        2 -> {
                            setAvatarData(avatar, levels, R.id.avatar2, R.id.nivel1avatar2, R.id.nivel2avatar2, R.id.nivel3avatar2)
                        }
                        3 -> {
                            setAvatarData(avatar, levels, R.id.avatar3, R.id.nivel1avatar3, R.id.nivel2avatar3, R.id.nivel3avatar3)
                        }
                        4 -> {
                            setAvatarData(avatar, levels, R.id.avatar4, R.id.nivel1avatar4, R.id.nivel2avatar4, R.id.nivel3avatar4)
                        }
                        5 -> {
                            setAvatarData(avatar, levels, R.id.avatar5, R.id.nivel1avatar5, R.id.nivel2avatar5, R.id.nivel3avatar5)
                        }
                    }
                }
            }
        }
    }

    // Función que establece los datos del avatar (intentos y tiempo) en los TextViews correspondientes
    private fun setAvatarData(avatar: Int, levels: JSONArray, avatarTextId: Int, level1TextId: Int, level2TextId: Int, level3TextId: Int) {
        val avatarTextView = findViewById<TextView>(avatarTextId)  // TextView para el avatar
        val level1TextView = findViewById<TextView>(level1TextId)  // TextView para el nivel 1
        val level2TextView = findViewById<TextView>(level2TextId)  // TextView para el nivel 2
        val level3TextView = findViewById<TextView>(level3TextId)  // TextView para el nivel 3

        // Recorre los niveles del avatar y establece el texto correspondiente
        for (i in 0 until levels.length()) {
            val levelData = levels.getJSONObject(i)
            val level = levelData.getInt("level")  // Obtiene el nivel
            val time = levelData.getString("time")  // Obtiene el tiempo
            val attempts = levelData.getInt("attempts")  // Obtiene los intentos

            // Asigna los datos al TextView correspondiente según el nivel
            when (level) {
                1 -> {
                    level1TextView.text = "$attempts Intentos, $time"
                }
                2 -> {
                    level2TextView.text = "$attempts Intentos, $time"
                }
                3 -> {
                    level3TextView.text = "$attempts Intentos, $time"
                }
            }
        }
    }
}
