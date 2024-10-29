package com.example.memorygame

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, GameLevel1::class.java)
        val memoryButton = findViewById<Button>(R.id.memoryButton)
        val simonButton = findViewById<Button>(R.id.simonButton)

        simonButton.isEnabled = false
        simonButton.alpha = 0.5f

        memoryButton.setOnClickListener{
            startActivity(intent)
        }
    }
}
