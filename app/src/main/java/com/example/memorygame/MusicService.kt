package com.example.memorygame

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Inicializar MediaPlayer y reproducir la música
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        return START_STICKY // El servicio continuará ejecutándose incluso si la actividad cambia
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // No se necesita vinculación en este caso
    }

    override fun onDestroy() {
        mediaPlayer?.stop() // Detener la música cuando se destruye el servicio
        mediaPlayer?.release() // Liberar recursos
    }

}