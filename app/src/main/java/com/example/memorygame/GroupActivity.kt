package com.example.memorygame

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class GroupActivity : AppCompatActivity() {

    private lateinit var groupsJSON: JSONObject
    private var group: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        group = intent.getIntExtra("group", -1)
        if (group == -1) {
            Toast.makeText(this, "Grupo no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        groupsJSON = getGroups()  // Cargar los datos del archivo

        // Poblar la información en la UI
        loadGroupData(group!!)
    }

    private fun getGroups(): JSONObject {
        val file = File(this.filesDir, "player_data.json")
        val playerData = JSONObject(file.readText())
        return playerData  // Regresa todo el objeto JSON para trabajarlo más adelante
    }

    private fun loadGroupData(group: Int) {
        val groupsArray = groupsJSON.getJSONArray("groups")

        // Buscar el grupo especificado
        for (i in 0 until groupsArray.length()) {
            val groupObject = groupsArray.getJSONObject(i)
            if (groupObject.getInt("group") == group) {
                // Este es el grupo seleccionado, ahora poblar los datos
                val avatars = groupObject.getJSONArray("avatars")
                for (j in 0 until avatars.length()) {
                    val avatarObj = avatars.getJSONObject(j)
                    val avatar = avatarObj.getInt("avatar")
                    val levels = avatarObj.getJSONArray("levels")

                    // Dependiendo del avatar, asignamos los datos a los TextViews
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
                break  // Ya hemos encontrado el grupo, no es necesario seguir buscando
            }
        }
    }

    private fun setAvatarData(avatar: Int, levels: JSONArray, avatarTextId: Int, level1TextId: Int, level2TextId: Int, level3TextId: Int) {
        val avatarTextView = findViewById<TextView>(avatarTextId)
        val level1TextView = findViewById<TextView>(level1TextId)
        val level2TextView = findViewById<TextView>(level2TextId)
        val level3TextView = findViewById<TextView>(level3TextId)

        // Seteamos el nombre del avatar (ejemplo: Avatar 1, Avatar 2, etc.)
        avatarTextView.text = "Avatar $avatar"

        // Para cada nivel, actualizamos el texto correspondiente (si existe nivel 1, 2 y 3)
        for (i in 0 until levels.length()) {
            val levelData = levels.getJSONObject(i)
            val level = levelData.getInt("level")
            val time = levelData.getString("time")
            val attempts = levelData.getInt("attempts")

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
