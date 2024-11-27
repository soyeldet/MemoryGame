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

    lateinit var intent2: Intent
    lateinit var intent3: Intent

    @SuppressLint("MissingInflatedId", "SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restart_game)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )


        var level: Int? = intent.getIntExtra("level", -1)
        val seconds: Int? = intent.getIntExtra("seconds", -1)
        val minutes: Int? = intent.getIntExtra("minutes", -1)
        val attempts: Int? = intent.getIntExtra("attempts", -1)
        val avatar: Int? = intent.getIntExtra("avatar", -1)


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
            intent2 = Intent(this, GameActivity::class.java)
            intent3 = Intent(this, GameActivity::class.java)
        } else if (level == 2) {
            intent2 = Intent(this, GameActivity::class.java)
            intent3 = Intent(this, GameActivity::class.java)
        } else if (level == 3) {
            wonButton.setBackgroundResource(R.drawable.rounded_button_exit)
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.exit)
            wonButton.setImageBitmap(bitmap)

            intent3 = Intent(this, GameActivity::class.java)
            intent3 = Intent(this, MainMenuActivity::class.java)
        }

        wonButton.setOnClickListener {
            saveAvatarData(this, avatar ?: -1, level ?: 1, timeFormatted, attempts ?: 0)
            intent3.putExtra("avatar", avatar)
            level = level!!+1
            intent3.putExtra("level", level)
            startActivity(intent3)
        }

        restartButton.setOnClickListener {
            intent2.putExtra("avatar2", avatar)
            startActivity(intent2)
        }

    }

    fun saveAvatarData(context: Context, avatar: Int, level: Int, time: String, attempts: Int) {

        if (avatar == -1) {
            return
        }

        try {
            val file = File(context.filesDir, "player_data.json")

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

            if (currentGroup == null || currentGroup.getJSONArray("avatars").length() >= 5) {
                currentGroup = JSONObject().apply {
                    put("group", groups.length() + 1)
                    put("avatars", JSONArray())
                }
                groups.put(currentGroup)
            }
            val avatars = currentGroup.getJSONArray("avatars")

            var avatarExists = false
            for (i in 0 until avatars.length()) {
                val avatarData = avatars.getJSONObject(i)
                if (avatarData.getInt("avatar") == avatar) {
                    val levels = avatarData.getJSONArray("levels")
                    var levelExists = false

                    for (j in 0 until levels.length()) {
                        val levelData = levels.getJSONObject(j)
                        if (levelData.getInt("level") == level) {
                            levelData.put("time", time)
                            levelData.put("attempts", attempts)
                            levelExists = true
                            break
                        }
                    }

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

            FileOutputStream(file).use { outputStream ->
                outputStream.write(playerData.toString().toByteArray())
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getCurrentGroup(groups: JSONArray): JSONObject? {
        for (i in 0 until groups.length()) {
            val group = groups.getJSONObject(i)
            val avatars = group.getJSONArray("avatars")
            if (avatars.length() < 5) {
                return group
            }
        }
        return null
    }

}
