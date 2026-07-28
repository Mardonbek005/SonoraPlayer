package com.sonora.player.ui.equalizer

import androidx.lifecycle.ViewModel
import com.sonora.player.audiofx.AudioEffectManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class EqualizerBand(
    val bandId: Short,
    val frequency: Int,
    val level: Short
)

@HiltViewModel
class EqualizerViewModel @Inject constructor(
    private val audioEffectManager: AudioEffectManager
) : ViewModel() {

    private val _bands = MutableStateFlow<List<EqualizerBand>>(emptyList())
    val bands: StateFlow<List<EqualizerBand>> = _bands.asStateFlow()

    private val _minLevel = MutableStateFlow<Short>(0)
    val minLevel: StateFlow<Short> = _minLevel.asStateFlow()

    private val _maxLevel = MutableStateFlow<Short>(0)
    val maxLevel: StateFlow<Short> = _maxLevel.asStateFlow()

    private val _bassStrength = MutableStateFlow(0f)
    val bassStrength: StateFlow<Float> = _bassStrength.asStateFlow()

    init {
        loadEqualizerSettings()
    }

    private fun loadEqualizerSettings() {
        val numBands = audioEffectManager.getNumberOfBands()
        if (numBands > 0) {
            val range = audioEffectManager.getBandLevelRange()
            _minLevel.value = range[0]
            _maxLevel.value = range[1]

            val currentBands = mutableListOf<EqualizerBand>()
            for (i in 0 until numBands) {
                val bandId = i.toShort()
                currentBands.add(
                    EqualizerBand(
                        bandId = bandId,
                        frequency = audioEffectManager.getCenterFreq(bandId),
                        level = audioEffectManager.getBandLevel(bandId)
                    )
                )
            }
            _bands.value = currentBands
        }
    }

    fun updateBandLevel(bandId: Short, level: Short) {
        audioEffectManager.setBandLevel(bandId, level)
        _bands.value = _bands.value.map {
            if (it.bandId == bandId) it.copy(level = level) else it
        }
    }

    fun updateBassStrength(strength: Float) {
        _bassStrength.value = strength
        // BassBoost 0 dan 1000 gacha bo'lgan qiymat qabul qiladi
        audioEffectManager.setBassBoostStrength((strength * 1000).toInt().toShort())
    }
}
