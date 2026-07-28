package com.sonora.player.audiofx

import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [AudioEffectManager] pleyerning ovoz effektlarini (Equalizer, BassBoost) boshqaradi.
 */
@Singleton
class AudioEffectManager @Inject constructor(
    private val player: ExoPlayer
) {
    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null

    init {
        // Pleyerdan Audio Session ID o'zgarganini eshitib turish
        player.addListener(object : Player.Listener {
            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                if (audioSessionId != 0) {
                    initEffects(audioSessionId)
                }
            }
        })
    }

    private fun initEffects(sessionId: Int) {
        try {
            // Eskilarini xotiradan tozalaymiz (Memory leak bo'lmasligi uchun)
            equalizer?.release()
            bassBoost?.release()

            // Yangi effektlarni joriy sessiyaga ulaymiz
            equalizer = Equalizer(0, sessionId)
            bassBoost = BassBoost(0, sessionId)

            // Effektlarni yoqamiz
            equalizer?.enabled = true
            bassBoost?.enabled = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Equalizer Boshqaruvi ---

    fun getNumberOfBands(): Short = equalizer?.numberOfBands ?: 0

    fun getBandLevelRange(): ShortArray = equalizer?.bandLevelRange ?: shortArrayOf(0, 0)

    fun getCenterFreq(band: Short): Int = equalizer?.getCenterFreq(band) ?: 0

    fun getBandLevel(band: Short): Short = equalizer?.getBandLevel(band) ?: 0

    fun setBandLevel(band: Short, level: Short) {
        try {
            equalizer?.setBandLevel(band, level)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Bass Boost Boshqaruvi ---

    fun setBassBoostStrength(strength: Short) {
        try {
            if (bassBoost?.strengthSupported == true) {
                bassBoost?.setStrength(strength)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        equalizer?.release()
        bassBoost?.release()
        equalizer = null
        bassBoost = null
    }
}
