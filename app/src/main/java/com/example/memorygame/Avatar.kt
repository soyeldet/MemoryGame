package com.example.memorygame

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager


class Avatar : AppCompatActivity() {

    private lateinit var gridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avatars_layout)
        gridView = findViewById(R.id.gridVIew)

        /*
        val intent = Intent(this, GameLevel1::class.java)
        startActivity(intent)
         */






    }
}