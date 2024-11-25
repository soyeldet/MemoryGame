package com.example.memorygame

import com.example.memorygame.adapters.ImageAdapterRecyclerVIew
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.adapters.ImageAdapterGridView


class GameLevel3 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapterRecyclerVIew: ImageAdapterRecyclerVIew
    private lateinit var imageAdapterGridView: ImageAdapterGridView
    private lateinit var gridView: GridView
    private lateinit var frameLayout: FrameLayout
    private lateinit var winorlose: FrameLayout
    private var firstClickedPosition: Int? = null
    private var isClickable = true
    private var level: Int = 3
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var isRunning = false
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var attempts: Int = 0
    private var avatar: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_level)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        avatar = intent.getIntExtra("avatar2", -1)

        recyclerView = findViewById(R.id.recyclerView)
        gridView = findViewById(R.id.gridVIew)
        frameLayout = findViewById(R.id.frameLayout)
        winorlose = findViewById(R.id.winorlose)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.isScrollContainer = false
        gridView.isNestedScrollingEnabled = false
        winorlose.alpha = 0f

        handler = Handler()
        startTimer()

        val items: MutableList<Int?> = mutableListOf(
            R.drawable.n1, R.drawable.n1,
            R.drawable.n2, R.drawable.n2,
            R.drawable.n3, R.drawable.n3,
            R.drawable.n4, R.drawable.n4,
            R.drawable.n5, R.drawable.n5
        ).shuffled().toMutableList()

        val items2: MutableList<Int?> = MutableList(items.size / 2) { R.drawable.light_blue }

        gridView.numColumns = items2.size
        imageAdapterRecyclerVIew = ImageAdapterRecyclerVIew(items)
        imageAdapterGridView = ImageAdapterGridView(items2, items)
        recyclerView.adapter = imageAdapterRecyclerVIew
        gridView.adapter = imageAdapterGridView

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

    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            runnable = object : Runnable {
                override fun run() {
                    seconds++
                    if (seconds >= 60){
                        minutes++
                        seconds = 0
                    }
                    handler.postDelayed(this, 1000)
                }
            }
            handler.post(runnable)
        }
    }

    private fun onItemClick(position: Int, items: MutableList<Int?>) {

        if (items[position] == null || (firstClickedPosition != null && firstClickedPosition == position)) {
            return
        }

        if (firstClickedPosition == null) {
            firstClickedPosition = position
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as? ImageAdapterRecyclerVIew.ViewHolder
            holder?.let { imageAdapterRecyclerVIew.flipCard(it) }
        } else {
            val firstItem = items[firstClickedPosition!!]
            val secondItem = items[position]

            val firstHolder = recyclerView.findViewHolderForAdapterPosition(firstClickedPosition!!) as? ImageAdapterRecyclerVIew.ViewHolder
            val secondHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ImageAdapterRecyclerVIew.ViewHolder

            secondHolder?.let { imageAdapterRecyclerVIew.flipCard(it) }

            if (firstItem == secondItem) {
                Handler(Looper.getMainLooper()).postDelayed({
                    winorlose.alpha = 0.5f
                    winorlose.setBackgroundColor(ContextCompat.getColor(this, R.color.success_green))
                }, 700)
                attempts++
                Handler(Looper.getMainLooper()).postDelayed({
                    imageAdapterRecyclerVIew.remove(firstClickedPosition!!)
                    imageAdapterRecyclerVIew.remove(position)
                    imageAdapterGridView.addItem(firstItem!!)
                    firstClickedPosition = null

                    val allNull = items.all { it.toString() == "null" }

                    if (allNull) {
                        val intent = Intent(this, RestartGameActivity::class.java)
                        intent.putExtra("level", level)
                        intent.putExtra("seconds", seconds)
                        intent.putExtra("minutes", minutes)
                        intent.putExtra("attempts", attempts)
                        intent.putExtra("avatar", avatar)
                        startActivity(intent)
                    }
                    winorlose.alpha = 0f
                }, 1200)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    winorlose.alpha = 0.5f
                    winorlose.setBackgroundColor(ContextCompat.getColor(this, R.color.error_red))
                }, 700)
                attempts++
                Handler(Looper.getMainLooper()).postDelayed({
                    firstHolder?.let { imageAdapterRecyclerVIew.flipBackCard(it) }
                    secondHolder?.let { imageAdapterRecyclerVIew.flipBackCard(it) }
                    firstClickedPosition = null
                    winorlose.alpha = 0f
                }, 1200)
            }
        }
    }
}
