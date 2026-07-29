package com.sonora.player.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonora.player.player.SonoraAudioHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedPlayerViewModel @Inject constructor(
    private val audioHandler: SonoraAudioHandler
) : ViewModel() {

    val currentMediaItem = audioHandler.currentMediaItem
    val isPlaying = audioHandler.isPlaying

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    // Holatlar o'chib ketmasligi uchun ViewModel'da saqlanadi
    private val _isShuffleEnabled = MutableStateFlow(false)
    val isShuffleEnabled: StateFlow<Boolean> = _isShuffleEnabled.asStateFlow()

    private val _isRepeatEnabled = MutableStateFlow(false)
    val isRepeatEnabled: StateFlow<Boolean> = _isRepeatEnabled.asStateFlow()

    init {
        updateProgress()
    }

    private fun updateProgress() {
        viewModelScope.launch {
            while (isActive) {
                _currentPosition.value = audioHandler.currentPosition
                _duration.value = audioHandler.duration.coerceAtLeast(0L)
                delay(1000L) 
            }
        }
    }

    fun playOrPause() = audioHandler.playOrPause()
    fun skipToNext() = audioHandler.skipToNext()
    fun skipToPrevious() = audioHandler.skipToPrevious()
    
    fun seekTo(position: Float) {
        audioHandler.seekTo(position.toLong())
    }

    fun toggleShuffle() {
        val newState = !_isShuffleEnabled.value
        _isShuffleEnabled.value = newState
        audioHandler.setShuffleMode(newState)
    }

    fun toggleRepeat() {
        val newState = !_isRepeatEnabled.value
        _isRepeatEnabled.value = newState
        audioHandler.setRepeatMode(newState)
    }

    fun toggleMute() {
        audioHandler.toggleMute()
    }
}
