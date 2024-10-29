package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast

class RestartGame : AppCompatActivity() {

    lateinit var intent2: Intent
    lateinit var intent3: Intent

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.levelfinished)

        val level: Int? = intent.getIntExtra("level", -1)
        val seconds: Int? = intent.getIntExtra("seconds", -1)

        val wonButton = findViewById<Button>(R.id.wonButton)
        val restartButton = findViewById<Button>(R.id.restartButton)
        val levelText = findViewById<TextView>(R.id.levelText)
        val timeText = findViewById<TextView>(R.id.timeText)
        val attempsText = findViewById<TextView>(R.id.attemptsText)

        if (level == 1){
            intent2 = Intent(this, GameLevel1::class.java)
            intent3 = Intent(this, GameLevel2::class.java)
        } else if (level == 2){
            intent2 = Intent(this, GameLevel2::class.java)
            intent3 = Intent(this, GameLevel3::class.java)
        } else if (level == 3){
            wonButton.text = "EXIT"
            intent2 = Intent(this, GameLevel3::class.java)
            intent3 = Intent(this, MainActivity::class.java)
        }

        wonButton.setOnClickListener{
            startActivity(intent3)
        }

        restartButton.setOnClickListener{
            startActivity(intent2)
        }
    }
}