package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RestartGameActivity : AppCompatActivity() {

    lateinit var intent2: Intent  // Intent para reiniciar el juego
    lateinit var intent3: Intent  // Intent para ir al siguiente nivel o menú principal

    @SuppressLint("MissingInflatedId", "SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE  // Orientación a horizontal
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restart_game)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Recupera los datos pasados por el Intent desde la actividad anterior
        var level: Int? = intent.getIntExtra("level", -1)  // Nivel actual
        val seconds: Int? = intent.getIntExtra("seconds", -1)  // Segundos pasados
        val minutes: Int? = intent.getIntExtra("minutes", -1)  // Minutos pasados
        val attempts: Int? = intent.getIntExtra("attempts", -1)  // Número de intentos
        val avatar: Int? = intent.getIntExtra("avatar", -1)  // Avatar seleccionado por el jugador

        val wonButton = findViewById<ImageButton>(R.id.wonButton)
        val restartButton = findViewById<ImageButton>(R.id.restartButton)
        val levelText = findViewById<TextView>(R.id.levelText)
        val timeText = findViewById<TextView>(R.id.timeText)
        val attemptsText = findViewById<TextView>(R.id.attemptsText)

        // Muestra el nivel, intentos y tiempo
        levelText.text = "Nivel: $level"
        attemptsText.text = "Intentos : $attempts"
        timeText.text = "Tiempo: $minutes:$seconds"

        val timeFormatted = "$minutes:$seconds"  // Reseteamos el tiempo para mostrarlo

        // Configura los intents para reiniciar o avanzar
        if (level == 1) {
            intent2 = Intent(this, GameActivity::class.java)
            intent3 = Intent(this, GameActivity::class.java)
        } else if (level == 2) {
            intent2 = Intent(this, GameActivity::class.java)
            intent3 = Intent(this, GameActivity::class.java)
        } else if (level == 3) {
            // Si es el nivel 3, cambia el botón "siguiente" para permitir salir
            wonButton.setBackgroundResource(R.drawable.rounded_button_exit)
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.exit)
            wonButton.setImageBitmap(bitmap)

            val file = File(this.filesDir, "player_data.json")

            // Si el archivo existe, lee el contenido. Si no, crea uno nuevo
            val playerData = if (file.exists()) {
                val jsonString = file.readText()
                JSONObject(jsonString)
            } else {
                JSONObject().apply {
                    put("groups", JSONArray())
                }
            }

            val groups = playerData.getJSONArray("groups")

            var currentGroup = getCurrentGroup(groups)

            if (currentGroup != null) {
                if (currentGroup.getJSONArray("avatars").length() >= 5){
                    currentGroup = JSONObject().apply {
                        put("group", groups.length() + 1)
                        put("avatars", JSONArray())
                    }
                    groups.put(currentGroup)
                }
            }

            intent3 = Intent(this, GameActivity::class.java)
            intent3 = Intent(this, MainMenuActivity::class.java)
        }

        // Acción para cuando el jugador presiona el botón "siguiente"
        wonButton.setOnClickListener {
            // Guarda los datos del avatar y nivel antes de continuar
            saveAvatarData(this, avatar ?: -1, level ?: 1, timeFormatted, attempts ?: 0)
            intent3.putExtra("avatar", avatar)
            level = level!! + 1
            intent3.putExtra("level", level)
            startActivity(intent3)
        }

        // Botón "restart"
        restartButton.setOnClickListener {
            intent2.putExtra("avatar2", avatar)
            startActivity(intent2)
        }

    }

    // Guardamos los datos en un archivo JSON
    fun saveAvatarData(context: Context, avatar: Int, level: Int, time: String, attempts: Int) {
        if (avatar == -1) {
            return
        }

        try {
            // Abre el archivo JSON donde se guardan los datos del jugador
            val file = File(context.filesDir, "player_data.json")

            // Si el archivo existe, lee el contenido. Si no, crea uno nuevo
            val playerData = if (file.exists()) {
                val jsonString = file.readText()
                JSONObject(jsonString)
            } else {
                JSONObject().apply {
                    put("groups", JSONArray())
                }
            }

            val groups = playerData.getJSONArray("groups")

            // Buscamos un grupo disponible para agregar el avatar
            var currentGroup = getCurrentGroup(groups)

            // Si no hay grupo disponible o el grupo tiene 5 avatares, crearemos uno nuevo
            if (currentGroup == null) {
                currentGroup = JSONObject().apply {
                    put("group", groups.length() + 1)
                    put("avatars", JSONArray())
                }
                groups.put(currentGroup)
            }
            val avatars = currentGroup.getJSONArray("avatars")

            var avatarExists = false
            // Busca si el avatar ya está registrado en el grupo
            for (i in 0 until avatars.length()) {
                val avatarData = avatars.getJSONObject(i)
                if (avatarData.getInt("avatar") == avatar) {
                    val levels = avatarData.getJSONArray("levels")
                    var levelExists = false

                    // Verifica si el nivel ya está registrado para este avatar
                    for (j in 0 until levels.length()) {
                        val levelData = levels.getJSONObject(j)
                        if (levelData.getInt("level") == level) {
                            levelData.put("time", time)
                            levelData.put("attempts", attempts)
                            levelExists = true
                            break
                        }
                    }

                    // Si no existe el nivel, lo agregamos
                    if (!levelExists) {
                        val levelData = JSONObject().apply {
                            put("level", level)
                            put("time", time)
                            put("attempts", attempts)
                        }
                        levels.put(levelData)
                    }
                    avatarExists = true
                    break
                }
            }

            // Si el avatar no existe, lo agregamos con sus datos
            if (!avatarExists) {
                val avatarData = JSONObject().apply {
                    put("avatar", avatar)
                    val levels = JSONArray()

                    val levelData = JSONObject().apply {
                        put("level", level)
                        put("time", time)
                        put("attempts", attempts)
                    }

                    levels.put(levelData)
                    put("levels", levels)
                }
                avatars.put(avatarData)
            }

            // Guarda los datos actualizados en el archivo
            FileOutputStream(file).use { outputStream ->
                outputStream.write(playerData.toString().toByteArray())
            }

        } catch (e: IOException) {
            e.printStackTrace()  // En caso de error, imprime el stack trace
        }
    }

    // Función para encontrar un grupo con menos de 5 avatares
    fun getCurrentGroup(groups: JSONArray): JSONObject? {
        for (i in 0 until groups.length()) {
            val group = groups.getJSONObject(i)
            val avatars = group.getJSONArray("avatars")
            // Si hay espacio en el grupo, lo devuelve
            if (avatars.length() < 5) {
                return group
            }
        }
        return null  // Si no hay espacio, retorna null
    }

}
