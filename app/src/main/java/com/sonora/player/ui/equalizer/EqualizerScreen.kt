package com.sonora.player.ui.equalizer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.roundToInt

@Composable
fun EqualizerScreen(
    viewModel: EqualizerViewModel = hiltViewModel()
) {
    val bands by viewModel.bands.collectAsState()
    val minLevel by viewModel.minLevel.collectAsState()
    val maxLevel by viewModel.maxLevel.collectAsState()
    val bassStrength by viewModel.bassStrength.collectAsState()
    
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Ovoz Sozlamalari (Equalizer)",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Bass Boost boshqaruvi
        Text(
            text = "Bass Boost",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Slider(
            value = bassStrength,
            onValueChange = { viewModel.updateBassStrength(it) },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Equalizer chastotalari (Bands)
        if (bands.isNotEmpty()) {
            Text(
                text = "Chastotalar",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            bands.forEach { band ->
                // Gerts (Hz) formatiga o'tkazish
                val freqHz = band.frequency / 1000
                val freqText = if (freqHz >= 1000) "${freqHz / 1000} kHz" else "$freqHz Hz"

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = freqText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${(band.level / 100)} dB",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Slider(
                        value = band.level.toFloat(),
                        onValueChange = { newValue ->
                            viewModel.updateBandLevel(band.bandId, newValue.roundToInt().toShort())
                        },
                        valueRange = minLevel.toFloat()..maxLevel.toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else {
            Text(
                text = "Musiqa yoqilmagan yoki qurilmangiz ushbu effektni qo'llab-quvvatlamaydi.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
