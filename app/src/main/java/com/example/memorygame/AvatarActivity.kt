package com.example.memorygame

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AvatarActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var imageAdapter: ImageAdapter3

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avatars_layout)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        gridView = findViewById(R.id.gridViewAvatars)

        val file = File(this.filesDir, "player_data.json")

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

        val avatar1 = R.drawable.a1
        val avatar2 = R.drawable.a2
        val avatar3 = R.drawable.a3
        val avatar4 = R.drawable.a4
        val avatar5 = R.drawable.a5


        val avatarsJSON = currentGroup.getJSONArray("avatars")

        val avatars: MutableList<Int?> = mutableListOf(avatar1, avatar2, avatar3, avatar4, avatar5)

        val usedAvatars: MutableList<Int?> = mutableListOf()

        for (i in 0 until avatarsJSON.length()) {
            val avatar = avatarsJSON.getJSONObject(i)
            val avatarId = avatar.getInt("avatar")
            usedAvatars.add(avatarId)
        }

        usedAvatars.filterNotNull().sorted()
        usedAvatars.sortBy { it }

        for (i in 0 until avatarsJSON.length()){
            val avatarToRemove = when (usedAvatars[i]) {
                1 -> avatar1
                2 -> avatar2
                3 -> avatar3
                4 -> avatar4
                5 -> avatar5
                else -> null
            }

            avatarToRemove?.let {
                avatars.remove(it)
            }
        }

        imageAdapter = ImageAdapter3(avatars)
        gridView.adapter = imageAdapter


        gridView.setOnItemClickListener{
        _, _, position, _ ->

            val selectedImageName = when (position) {
                0 -> 1
                1 -> 2
                2 -> 3
                3 -> 4
                4 -> 5
                else -> "default"
            }

        val intent = Intent(this, GameLevel1::class.java)
        intent.putExtra("avatar", selectedImageName)
        startActivity(intent)
    }
    }
    private fun getCurrentGroup(groups: JSONArray): JSONObject? {
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