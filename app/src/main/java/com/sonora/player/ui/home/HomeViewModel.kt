package com.sonora.player.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.sonora.player.data.local.entity.SongEntity
import com.sonora.player.data.repository.MusicRepository
import com.sonora.player.player.SonoraAudioHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val songs: List<SongEntity> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val audioHandler: SonoraAudioHandler
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch {
            musicRepository.getAllSongs()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { songList ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        songs = songList,
                        errorMessage = null
                    )
                }
        }
    }

    fun syncMusic() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            musicRepository.syncMusicFromDevice()
            // Skanerlash tugagach, DB yangilanadi va Flow (loadSongs) avtomatik UI ni yangilaydi
        }
    }

    fun playSong(song: SongEntity, allSongs: List<SongEntity>) {
        val mediaItems = allSongs.map { entity ->
            MediaItem.Builder()
                .setMediaId(entity.mediaId)
                .setUri(entity.data)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(entity.title)
                        .setArtist(entity.artist)
                        .setAlbumTitle(entity.album)
                        .setArtworkUri(android.net.Uri.parse(entity.artworkUri))
                        .build()
                )
                .build()
        }

        val startIndex = allSongs.indexOf(song).takeIf { it >= 0 } ?: 0
        audioHandler.setMediaItems(mediaItems, startIndex)
    }
}
