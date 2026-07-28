package com.sonora.player.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    /**
     * Ovoz xususiyatlarini sozlaymiz. Bu orqali tizim musiqani to'g'ri taniydi
     * va bildirishnomalar kelganda ovozni pasaytiradi (Audio Focus).
     */
    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    /**
     * Pleyerning o'zini yaratamiz.
     * HandleAudioBecomingNoisy = true: Quloqchin uzilib ketganda musiqa avtomatik to'xtaydi.
     */
    @Provides
    @Singleton
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true) // Eshitish vositasi uzilsa pauza
            .build()
    }
}
