package com.example.memorygame

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Verificamos la acción que se pasa desde el Intent
        when (intent?.action) {
            "START_MUSIC" -> {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this, R.raw.musica)  // Asegúrate de usar tu archivo de música
                    mediaPlayer?.isLooping = true
                    mediaPlayer?.start()
                }
            }
            "STOP_MUSIC" -> {
                mediaPlayer?.stop()  // Detener la música
                mediaPlayer?.release()  // Liberar los recursos
                mediaPlayer = null
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