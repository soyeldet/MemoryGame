package com.example.memorygame

import com.example.memorygame.adapters.ImageAdapterRecyclerVIew
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.SoundPool
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

class GameActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapterRecyclerVIew: ImageAdapterRecyclerVIew
    private lateinit var imageAdapterGridView: ImageAdapterGridView
    private lateinit var gridView: GridView
    private lateinit var frameLayout: FrameLayout
    private lateinit var winorlose: FrameLayout
    private var firstClickedPosition: Int? = null
    private var level: Int = 1
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var isRunning = false
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var attempts: Int = 0
    private var isClickable = true
    private var avatar: Int = 0
    private var items: MutableList<Int?> = mutableListOf()

    // Efectos de sonido
    private lateinit var soundPool: SoundPool
    private var correctSound: Int = 0
    private var incorrectSound: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_level)

        // Ocultamos la barra de estado y navegación en modo pantalla completa e inmersiva
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Recibe los parámetros (avatar, nivel) de la actividad anterior
        avatar = intent.getIntExtra("avatar", -1)
        level = intent.getIntExtra("level", 1)

        recyclerView = findViewById(R.id.recyclerView)
        gridView = findViewById(R.id.gridVIew)
        frameLayout = findViewById(R.id.frameLayout)
        winorlose = findViewById(R.id.winorlose)

        // Configura el RecyclerView para mostrar las cartas en una fila de usuario
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.isScrollContainer = false
        gridView.isNestedScrollingEnabled = false
        winorlose.alpha = 0f

        // Inicializa el Handler y el cronómetro.
        handler = Handler()
        startTimer()

        // Ajustamos columnas y filas dependiendo del nivel
        if (level == 1) {
            val gridLayoutManager = GridLayoutManager(this, 3)
            recyclerView.layoutManager = gridLayoutManager

            items = mutableListOf(
                R.drawable.q1, R.drawable.q1,
                R.drawable.q2, R.drawable.q2,
                R.drawable.q3, R.drawable.q3
            ).shuffled().toMutableList()
        } else if (level == 2) {
            val gridLayoutManager = GridLayoutManager(this, 4)
            recyclerView.layoutManager = gridLayoutManager

            items = mutableListOf(
                R.drawable.c1, R.drawable.c1,
                R.drawable.c2, R.drawable.c2,
                R.drawable.c3, R.drawable.c3,
                R.drawable.c4, R.drawable.c4,
            ).shuffled().toMutableList()
        } else if (level == 3) {
            val gridLayoutManager = GridLayoutManager(this, 5)
            recyclerView.layoutManager = gridLayoutManager

            items = mutableListOf(
                R.drawable.n1, R.drawable.n1,
                R.drawable.n2, R.drawable.n2,
                R.drawable.n3, R.drawable.n3,
                R.drawable.n4, R.drawable.n4,
                R.drawable.n5, R.drawable.n5
            ).shuffled().toMutableList()
        }

        // Crea una lista con los elementos del fondo
        val items2: MutableList<Int?> = MutableList(items.size / 2) { R.drawable.light_blue }

        gridView.numColumns = items2.size

        imageAdapterRecyclerVIew = ImageAdapterRecyclerVIew(items)
        imageAdapterGridView = ImageAdapterGridView(items2, items)
        recyclerView.adapter = imageAdapterRecyclerVIew
        gridView.adapter = imageAdapterGridView

        // Detecta clics para girar las cartas
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
                        }, 1200)
                        onItemClick(position, items)
                    }
                    return super.onInterceptTouchEvent(recyclerView, motionEvent)
                }
                return true
            }
        })

        // Inicializa el SoundPool para manejar los efectos de sonido (éxito y error)
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()

        correctSound = soundPool.load(this, R.raw.success, 1)
        incorrectSound = soundPool.load(this, R.raw.fail, 1)
    }

    // Función para iniciar el cronómetro
    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            runnable = object : Runnable {
                override fun run() {
                    seconds++
                    if (seconds >= 60) {
                        minutes++
                        seconds = 0
                    }
                    handler.postDelayed(this, 1000)
                }
            }
            handler.post(runnable)
        }
    }

    // Función al hacer clic en una carta
    private fun onItemClick(position: Int, items: MutableList<Int?>) {
        // Si el elemento ya ha sido "descubierto" o es el mismo que el primero seleccionado, no hace nada.
        if (items[position] == null || (firstClickedPosition != null && firstClickedPosition == position)) {
            return
        }

        // Giramos la carta
        if (firstClickedPosition == null) {
            firstClickedPosition = position
            val holder =
                recyclerView.findViewHolderForAdapterPosition(position) as? ImageAdapterRecyclerVIew.ViewHolder
            holder?.let {
                imageAdapterRecyclerVIew.flipCard(it)
            }
        } else {
            // Giramos la carta y verificamos si es la correcta
            val firstItem = items[firstClickedPosition!!]
            val secondItem = items[position]

            val firstHolder =
                recyclerView.findViewHolderForAdapterPosition(firstClickedPosition!!) as? ImageAdapterRecyclerVIew.ViewHolder
            val secondHolder =
                recyclerView.findViewHolderForAdapterPosition(position) as? ImageAdapterRecyclerVIew.ViewHolder

            secondHolder?.let {
                imageAdapterRecyclerVIew.flipCard(it)
            }

            if (firstItem == secondItem) {
                // Si las cartas coinciden, mostramos el color verde y el sonido de acierto
                Handler(Looper.getMainLooper()).postDelayed({
                    winorlose.alpha = 0.5f
                    winorlose.setBackgroundColor(ContextCompat.getColor(this, R.color.success_green))
                    Handler(Looper.getMainLooper()).postDelayed({
                        soundPool.play(correctSound, 0.5f, 0.5f, 0, 0, 1f)
                    }, 350)
                }, 700)
                attempts++
                // Quitamos las parejas acertadas
                Handler(Looper.getMainLooper()).postDelayed({
                    imageAdapterRecyclerVIew.remove(firstClickedPosition!!)
                    imageAdapterRecyclerVIew.remove(position)
                    imageAdapterGridView.addItem(firstItem!!)
                    firstClickedPosition = null

                    // Al acabarse las cartas terminamos el nivel y tenemos la pantalla de "seguir"/"reiniciar"
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
                // Si las cartas no coinciden, mostramos el color rojo y el sonido de error
                Handler(Looper.getMainLooper()).postDelayed({
                    winorlose.alpha = 0.5f
                    winorlose.setBackgroundColor(ContextCompat.getColor(this, R.color.error_red))
                    Handler(Looper.getMainLooper()).postDelayed({
                        soundPool.play(incorrectSound, 0.5f, 0.5f, 0, 0, 1f)
                    }, 350)
                }, 700)
                attempts++
                // Damos la vuelta a las cartas si no coinciden
                Handler(Looper.getMainLooper()).postDelayed({
                    firstHolder?.let { imageAdapterRecyclerVIew.flipBackCard(it) }
                    secondHolder?.let { imageAdapterRecyclerVIew.flipBackCard(it) }
                    firstClickedPosition = null
                    winorlose.alpha = 0f
                }, 1200)
            }
        }
    }

    // Liberamos los recursos de sonido
    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
