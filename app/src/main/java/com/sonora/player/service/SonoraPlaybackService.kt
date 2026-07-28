package com.sonora.player.service

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fonda musiqa ijro etish va tizim bildirishnomalarini boshqaruvchi xizmat.
 * @AndroidEntryPoint Hilt orqali ExoPlayer'ni avtomatik olib keladi.
 */
@AndroidEntryPoint
class SonoraPlaybackService : MediaSessionService() {

    @Inject
    lateinit var player: ExoPlayer

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        
        // MediaSession'ni yaratamiz va uni Hilt bergan pleyerga bog'laymiz
        mediaSession = MediaSession.Builder(this, player)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        // Tizim (Lock screen, Android Auto, Bluetooth) sessiyani so'raganda shuni beramiz
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // Foydalanuvchi ilovani "Recent Apps" dan surib yuborganda, 
        // agar musiqa o'ynab turmagan bo'lsa, xizmatni butunlay to'xtatamiz.
        val player = mediaSession?.player
        if (player != null && !player.playWhenReady) {
            stopSelf()
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release() // RAM'ni tozalaymiz
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
