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
import androidx.core.content.ContextCompat
import android.content.Context
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RestartGame : AppCompatActivity() {

    lateinit var intent2: Intent
    lateinit var intent3: Intent

    @SuppressLint("MissingInflatedId", "SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.levelfinished)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )


        val level: Int? = intent.getIntExtra("level", -1)
        val seconds: Int? = intent.getIntExtra("seconds", -1)
        val minutes: Int? = intent.getIntExtra("minutes", -1)
        val attempts: Int? = intent.getIntExtra("attempts", -1)
        val avatar: Int? = intent.getIntExtra("avatar", -1)

        if (avatar != null) {
            Toast.makeText(this, avatar.toString(), Toast.LENGTH_SHORT).show()
        }

        val wonButton = findViewById<ImageButton>(R.id.wonButton)
        val restartButton = findViewById<ImageButton>(R.id.restartButton)
        val levelText = findViewById<TextView>(R.id.levelText)
        val timeText = findViewById<TextView>(R.id.timeText)
        val attemptsText = findViewById<TextView>(R.id.attemptsText)

        levelText.text = "Nivel: $level"
        attemptsText.text = "Intentos : $attempts"
        timeText.text = "Tiempo: $minutes:$seconds"

        val timeFormatted = "$minutes:$seconds"

        if (level == 1) {
            intent2 = Intent(this, GameLevel1::class.java)
            intent3 = Intent(this, GameLevel2::class.java)
        } else if (level == 2) {
            intent2 = Intent(this, GameLevel2::class.java)
            intent3 = Intent(this, GameLevel3::class.java)
        } else if (level == 3) {
            wonButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.exit)
            wonButton.setImageBitmap(bitmap)


            intent2 = Intent(this, GameLevel3::class.java)
            intent3 = Intent(this, MainActivity::class.java)
        }

        wonButton.setOnClickListener {
            saveAvatarData(this, avatar ?: -1, level ?: 1, timeFormatted, attempts ?: 0)
            intent3.putExtra("avatar2", avatar)
            startActivity(intent3)
        }

        restartButton.setOnClickListener {
            intent2.putExtra("avatar2", avatar)
            startActivity(intent2)
        }

    }

    fun saveAvatarData(context: Context, avatar: Int, level: Int, time: String, attempts: Int) {
        // Verificar si el avatar es válido (no -1)
        if (avatar == -1) {
            return  // No guardar si el avatar es inválido
        }

        try {
            val file = File(context.filesDir, "player_data.json")

            // Si el archivo no existe, inicializamos una estructura básica
            val playerData = if (file.exists()) {
                val jsonString = file.readText()
                JSONObject(jsonString)
            } else {
                JSONObject().apply {
                    put("groups", JSONArray())  // Contenedor de grupos
                }
            }

            // Obtener el array de grupos
            val groups = playerData.getJSONArray("groups")

            // Verificamos si ya existe un grupo con menos de 5 avatares, si no, creamos uno nuevo
            var currentGroup = getCurrentGroup(groups)

            if (currentGroup == null || currentGroup.getJSONArray("avatars").length() >= 5) {
                currentGroup = JSONObject().apply {
                    put("group", groups.length() + 1)
                    put("avatars", JSONArray())
                }
                groups.put(currentGroup)
            }

            // Obtener los avatares del grupo actual
            val avatars = currentGroup.getJSONArray("avatars")

            // Verificar si ya existe el avatar en el grupo
            var avatarExists = false
            for (i in 0 until avatars.length()) {
                val avatarData = avatars.getJSONObject(i)
                if (avatarData.getInt("avatar") == avatar) {
                    // Si el avatar ya existe, buscamos el nivel en la lista de niveles
                    val levels = avatarData.getJSONArray("levels")
                    var levelExists = false

                    // Verificamos si ya existe el nivel. Si es así, lo actualizamos.
                    for (j in 0 until levels.length()) {
                        val levelData = levels.getJSONObject(j)
                        if (levelData.getInt("level") == level) {
                            // Si el nivel ya existe, lo actualizamos
                            levelData.put("time", time)
                            levelData.put("attempts", attempts)
                            levelExists = true
                            break
                        }
                    }

                    // Si el nivel no existía, lo agregamos como un nuevo nivel
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

            // Si el avatar no existe en el grupo, lo creamos
            if (!avatarExists) {
                val avatarData = JSONObject().apply {
                    put("avatar", avatar)
                    val levels = JSONArray()

                    // Agregar el primer nivel
                    val levelData = JSONObject().apply {
                        put("level", level)
                        put("time", time)
                        put("attempts", attempts)
                    }

                    levels.put(levelData)  // Agregar el primer nivel
                    put("levels", levels)
                }
                avatars.put(avatarData)
            }

            // Guardar los datos de vuelta en el archivo
            FileOutputStream(file).use { outputStream ->
                outputStream.write(playerData.toString().toByteArray())
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    // Función para obtener el grupo actual o `null` si no existe un grupo adecuado
    fun getCurrentGroup(groups: JSONArray): JSONObject? {
        for (i in 0 until groups.length()) {
            val group = groups.getJSONObject(i)
            val avatars = group.getJSONArray("avatars")
            if (avatars.length() < 5) {
                return group  // Retorna el primer grupo con menos de 5 avatares
            }
        }
        return null  // No hay grupo disponible
    }

}
