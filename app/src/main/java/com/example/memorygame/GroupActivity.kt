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

    private lateinit var groupsJSON: JSONObject
    private var group: Int? = null
    private lateinit var backButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        backButton = findViewById(R.id.backButton)

        group = intent.getIntExtra("group", -1)
        if (group == -1) {
            Toast.makeText(this, "Grupo no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        groupsJSON = getGroups()
        loadGroupData(group!!)

        backButton.setOnClickListener{
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getGroups(): JSONObject {
        val file = File(this.filesDir, "player_data.json")
        val playerData = JSONObject(file.readText())
        return playerData
    }

    private fun loadGroupData(group: Int) {
        val groupsArray = groupsJSON.getJSONArray("groups")

        for (i in 0 until groupsArray.length()) {
            val groupObject = groupsArray.getJSONObject(i)
            if (groupObject.getInt("group") == group) {
                val avatars = groupObject.getJSONArray("avatars")
                for (j in 0 until avatars.length()) {
                    val avatarObj = avatars.getJSONObject(j)
                    val avatar = avatarObj.getInt("avatar")
                    val levels = avatarObj.getJSONArray("levels")

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

    private fun setAvatarData(avatar: Int, levels: JSONArray, avatarTextId: Int, level1TextId: Int, level2TextId: Int, level3TextId: Int) {
        val avatarTextView = findViewById<TextView>(avatarTextId)
        val level1TextView = findViewById<TextView>(level1TextId)
        val level2TextView = findViewById<TextView>(level2TextId)
        val level3TextView = findViewById<TextView>(level3TextId)

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
