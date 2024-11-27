package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AdminPanelActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var groupsJSON: MutableList<Int?>
    private var groups: MutableList<String?> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        gridView = findViewById(R.id.gridViewGroups)

        groupsJSON = getGroups();

        for (i in 0 until groupsJSON.size){
            groups.add("GRUPO " + (i+1))
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            groups.filterNotNull()
        )

        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, GroupActivity::class.java)
            intent.putExtra("group", position+1)
            startActivity(intent)
        }
        }


    private fun getGroups(): MutableList<Int?> {
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

        val groupList = mutableListOf<Int?>()
        for (i in 0 until groups.length()) {
            groupList.add(groups.optInt(i))
        }
        return groupList
    }
}