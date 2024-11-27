package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class DeleteJSONActivity : AppCompatActivity() {
    private lateinit var yesButton: Button
    private lateinit var noButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_json)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        yesButton = findViewById(R.id.yesButton)
        noButton = findViewById(R.id.noButton)




        noButton.setOnClickListener{
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
        }

        yesButton.setOnClickListener {
            val file = File(this.filesDir, "player_data.json")
            file.delete()
            val intent = Intent(this, AdminPanelActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Grupos eliminados correctamente", Toast.LENGTH_SHORT).show();

        }
    }
}