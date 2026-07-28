package com.sonora.player.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun NowPlayingScreen(
    navController: NavController,
    viewModel: SharedPlayerViewModel = hiltViewModel()
) {
    val currentMediaItem by viewModel.currentMediaItem.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    
    // Hozircha UI da ko'rsatish uchun vaqt (Keyinchalik ExoPlayer progressi bilan sinxronlanadi)
    val currentPosition = 0f
    val duration = 100f 

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Yuqori qism: Orqaga qaytish va menyu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Text(text = "▼", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    text = "Now Playing",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                IconButton(onClick = { /* TODO: Qo'shimcha menyu */ }) {
                    Text(text = "⋮", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Katta Musiqa Muqovasi (Album Art)
            val artworkUri = currentMediaItem?.mediaMetadata?.artworkUri
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artworkUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Large Album Art",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Kvadrat shaklida bo'lishi uchun
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Musiqa nomi va San'atkor
            Text(
                text = currentMediaItem?.mediaMetadata?.title?.toString() ?: "Unknown Title",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = currentMediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown Artist",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Vaqt chizig'i (Progress Slider)
            Slider(
                value = currentPosition,
                onValueChange = { /* TODO: Seek function */ },
                valueRange = 0f..duration,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "0:00", style = MaterialTheme.typography.labelMedium)
                Text(text = "4:20", style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Asosiy boshqaruv tugmalari (Play/Pause, Next, Prev)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.skipToPrevious() }, modifier = Modifier.size(56.dp)) {
                    Text(text = "⏮", fontSize = 32.sp)
                }
                
                IconButton(
                    onClick = { viewModel.playOrPause() },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = if (isPlaying) "⏸" else "▶",
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                IconButton(onClick = { viewModel.skipToNext() }, modifier = Modifier.size(56.dp)) {
                    Text(text = "⏭", fontSize = 32.sp)
                }
            }
        }
    }
}
