package com.example.memorygame

import ImageAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GameLevel3 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageAdapter2: ImageAdapter2
    private lateinit var gridView: GridView
    private lateinit var frameLayout: FrameLayout
    private var firstClickedPosition: Int? = null
    private var isClickable = true
    private var level: Int = 3

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level03)
        recyclerView = findViewById(R.id.recyclerView)
        gridView = findViewById(R.id.gridVIew)
        frameLayout = findViewById(R.id.frameLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.isScrollContainer = false
        gridView.isNestedScrollingEnabled = false

        val items: MutableList<Int?> = mutableListOf(
            R.drawable.n1, R.drawable.n1,
            R.drawable.n2, R.drawable.n2,
            R.drawable.n3, R.drawable.n3,
            R.drawable.n4, R.drawable.n4,
            R.drawable.n5, R.drawable.n5
        ).shuffled().toMutableList()

        val items2: MutableList<Int?> = MutableList(items.size / 2) { R.drawable.black }

        gridView.numColumns = items2.size
        imageAdapter = ImageAdapter(items)
        imageAdapter2 = ImageAdapter2(items2, items)
        recyclerView.adapter = imageAdapter
        gridView.adapter = imageAdapter2

        recyclerView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView,
                motionEvent: MotionEvent
            ): Boolean {
                if (motionEvent.pointerCount > 1) {
                    return true
                }
                if (isClickable) {
                    val childView = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
                    if (childView != null && motionEvent.action == MotionEvent.ACTION_UP) {
                        val position = recyclerView.getChildAdapterPosition(childView)
                        isClickable = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            isClickable = true
                        },1200)
                        onItemClick(position, items)
                    }
                    return super.onInterceptTouchEvent(recyclerView, motionEvent)
                }
                return true
            }
        })

    }

    private fun onItemClick(position: Int, items: MutableList<Int?>) {

        if (items[position] == null || (firstClickedPosition != null && firstClickedPosition == position)) {
            return
        }

        if (firstClickedPosition == null) {
            firstClickedPosition = position
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as? ImageAdapter.ViewHolder
            holder?.let { imageAdapter.flipCard(it) }
        } else {
            val firstItem = items[firstClickedPosition!!]
            val secondItem = items[position]

            val firstHolder = recyclerView.findViewHolderForAdapterPosition(firstClickedPosition!!) as? ImageAdapter.ViewHolder
            val secondHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ImageAdapter.ViewHolder

            secondHolder?.let { imageAdapter.flipCard(it) }

            if (firstItem == secondItem) {
                Handler(Looper.getMainLooper()).postDelayed({
                    imageAdapter.remove(firstClickedPosition!!)
                    imageAdapter.remove(position)
                    imageAdapter2.addItem(firstItem!!)
                    firstClickedPosition = null

                    val allNull = items.all { it.toString() == "null" }

                    if (allNull) {
                        val intent = Intent(this, RestartGame::class.java)
                        intent.putExtra("level", level)
                        startActivity(intent)
                    }
                }, 1200)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    firstHolder?.let { imageAdapter.flipBackCard(it) }
                    secondHolder?.let { imageAdapter.flipBackCard(it) }
                    firstClickedPosition = null
                }, 1200)
            }
        }
    }

}
