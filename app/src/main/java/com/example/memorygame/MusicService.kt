package com.example.memorygame

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicPlaying = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sharedPrefs = getSharedPreferences("MusicPrefs", MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        // Verificamos la acción que se pasa desde el Intent
        when (intent?.action) {
            "START_MUSIC" -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.music)
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.start()
                    isMusicPlaying = true
                    editor.putBoolean("isMusicPlaying", true)
                    editor.apply()
                }
            }
            "STOP_MUSIC" -> {
                mediaPlayer?.stop()  // Detener la música
                mediaPlayer?.release()  // Liberar los recursos
                mediaPlayer = null
                isMusicPlaying = false
                editor.putBoolean("isMusicPlaying", false)  // Guardamos el estado
                editor.apply()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
