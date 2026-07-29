package com.sonora.player.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.sonora.player.service.SonoraPlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SonoraAudioHandler @Inject constructor(
    private val context: Context
) : Player.Listener {
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
    val currentMediaItem: StateFlow<MediaItem?> = _currentMediaItem.asStateFlow()

    // Vaqtni olish uchun yangi qo'shilgan qatorlar
    val currentPosition: Long
        get() = mediaController?.currentPosition ?: 0L

    val duration: Long
        get() = mediaController?.duration ?: 0L

    init {
        initializeController()
    }

    private fun initializeController() {
        val sessionToken = SessionToken(
            context,
            ComponentName(context, SonoraPlaybackService::class.java)
        )
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture?.addListener(
            {
                mediaController = mediaControllerFuture?.get()
                mediaController?.addListener(this)
            },
            MoreExecutors.directExecutor()
        )
    }

    fun playOrPause() {
        mediaController?.let { controller ->
            if (controller.isPlaying) {
                controller.pause()
            } else {
                controller.play()
            }
        }
    }

    fun skipToNext() {
        mediaController?.seekToNext()
    }

    fun skipToPrevious() {
        mediaController?.seekToPrevious()
    }

    fun setMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        mediaController?.setMediaItems(mediaItems, startIndex, 0)
        mediaController?.prepare()
        mediaController?.play()
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        _currentMediaItem.value = mediaItem
    }

    fun release() {
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
        mediaController?.removeListener(this)
    }
}
