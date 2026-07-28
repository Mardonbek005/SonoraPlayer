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

    // Vaqt chizig'i (Progress) uchun holatlar
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    init {
        updateProgress()
    }

    private fun updateProgress() {
        viewModelScope.launch {
            while (isActive) {
                if (isPlaying.value) {
                    // ExoPlayer yordamida hozirgi vaqtni olish maqsadga muvofiq
                    // Lekin biz AudioHandler orqali bog'langanmiz. Hozircha vaqtni simulyatsiya qilamiz 
                    // yoki AudioHandler'dan joriy progressni olib keluvchi funksiyani chaqiramiz.
                    // (To'liq ExoPlayer obyektiga kirish uchun AudioHandler kengaytirilishi kerak).
                    // Hozirgi arxitekturada UI ni qotirmaslik uchun Flow ishlatdik.
                }
                delay(1000L) // Har 1 soniyada yangilanadi (UI uchun optimal)
            }
        }
    }

    fun playOrPause() = audioHandler.playOrPause()
    
    fun skipToNext() = audioHandler.skipToNext()
    
    fun skipToPrevious() = audioHandler.skipToPrevious()
    
    fun seekTo(position: Long) = audioHandler.seekTo(position)
}

