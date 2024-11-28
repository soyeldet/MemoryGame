package com.example.memorygame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.memorygame.adapters.ImageAdapterAvatarsGV
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AvatarsActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var imageAdapter: ImageAdapterAvatarsGV
    private lateinit var avatars: MutableList<Int?>
    private lateinit var volumeButton: ImageButton
    private var volume = true

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatars)

        // Configura la interfaz para ocultar la barra de estado y navegación en modo pantalla completa e inmersiva.
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        // Crea un Intent para interactuar con el servicio de música.
        val musicServiceIntent = Intent(this, MusicService::class.java)

        // Encuentra los elementos del layout: el GridView y el botón de volumen.
        gridView = findViewById(R.id.gridViewAvatars)
        volumeButton = findViewById(R.id.volumeButton)

        // Obtiene los avatares desde el archivo o base de datos.
        avatars = getAvatars()

        // Crea el adaptador para el GridView y lo asocia.
        imageAdapter = ImageAdapterAvatarsGV(avatars)
        gridView.adapter = imageAdapter

        // Obtiene las preferencias compartidas para controlar el volumen de la música.
        val sharedPrefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE)
        volume = sharedPrefs.getBoolean("isMusicPlaying", false)

        // Establece la imagen del botón de volumen según el estado actual (activado o silenciado).
        if (volume) {
            volumeButton.setImageResource(R.drawable.volume)
        } else {
            volumeButton.setImageResource(R.drawable.volume_mute)
        }

        // Configura el comportamiento al hacer clic en un ítem del GridView (selección de avatar).
        gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedImageName = when (position) {
                0 -> 1
                1 -> 2
                2 -> 3
                3 -> 4
                4 -> 5
                else -> "default"
            }

            // Crea un Intent para pasar el avatar seleccionado a la actividad del juego.
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("avatar", selectedImageName)
            startActivity(intent)
        }

        // Configura el comportamiento del botón de volumen para activar/desactivar la música.
        volumeButton.setOnClickListener {
            if (volume) {
                // Detiene la música si está activa y cambia la imagen del botón de volumen.
                musicServiceIntent.action = "STOP_MUSIC"
                startService(musicServiceIntent)
                volumeButton.setImageResource(R.drawable.volume_mute)
                volume = false
            } else {
                // Inicia la música si está desactivada y cambia la imagen del botón de volumen.
                musicServiceIntent.action = "START_MUSIC"
                startService(musicServiceIntent)
                volumeButton.setImageResource(R.drawable.volume)
                volume = true
            }
        }
    }

    // Función que obtiene los avatares disponibles desde un archivo JSON.
    private fun getAvatars(): MutableList<Int?> {
        val file = File(this.filesDir, "player_data.json")

        // Lee el archivo JSON o crea un nuevo objeto vacío si no existe.
        val playerData = if (file.exists()) {
            val jsonString = file.readText()
            JSONObject(jsonString)
        } else {
            JSONObject().apply {
                put("groups", JSONArray())
            }
        }

        // Obtiene el grupo de avatares actuales.
        val groups = playerData.getJSONArray("groups")
        var currentGroup = getCurrentGroup(groups)

        // Asigna los avatares predeterminados (del 1 al 5).
        val avatar1 = R.drawable.a1
        val avatar2 = R.drawable.a2
        val avatar3 = R.drawable.a3
        val avatar4 = R.drawable.a4
        val avatar5 = R.drawable.a5

        // Obtiene los avatares usados en el grupo actual y filtra los que están en uso.
        val avatarsJSON = currentGroup?.optJSONArray("avatars") ?: JSONArray()
        val avatars: MutableList<Int?> = mutableListOf(avatar1, avatar2, avatar3, avatar4, avatar5)
        val usedAvatars: MutableList<Int?> = mutableListOf()

        // Llena la lista de avatares usados.
        for (i in 0 until avatarsJSON.length()) {
            val avatar = avatarsJSON.getJSONObject(i)
            val avatarId = avatar.getInt("avatar")
            usedAvatars.add(avatarId)
        }

        // Ordena los avatares usados.
        usedAvatars.filterNotNull().sorted()
        usedAvatars.sortBy { it }

        // Elimina los avatares usados de la lista de avatares disponibles.
        for (i in 0 until avatarsJSON.length()) {
            val avatarToRemove = when (usedAvatars[i]) {
                1 -> avatar1
                2 -> avatar2
                3 -> avatar3
                4 -> avatar4
                5 -> avatar5
                else -> null
            }

            avatarToRemove?.let {
                val index = avatars.indexOf(it)
                if (index != -1) {
                    avatars[index] = Color.TRANSPARENT
                }
            }
        }

        return avatars
    }

    // Función para obtener el grupo actual que tenga menos de 5 avatares.
    private fun getCurrentGroup(groups: JSONArray): JSONObject? {
        for (i in 0 until groups.length()) {
            val group = groups.getJSONObject(i)
            val avatars = group.getJSONArray("avatars")
            if (avatars.length() < 5) {
                return group
            }
        }
        val newGroup = JSONObject().apply {
            put("group", groups.length() + 1)  // Asigna un número único al nuevo grupo
            put("avatars", JSONArray())  // Inicializa el array de avatares vacío
        }

        // Agrega el nuevo grupo al final de la lista de grupos
        groups.put(newGroup)

        // Devuelve el nuevo grupo
        return newGroup
    }
}
