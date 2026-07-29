package com.sonora.player.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()

    fun formatTime(ms: Long): String {
        if (ms < 0) return "00:00"
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Haqiqiy qora fon
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                // Pastdagi mini-player ustiga chiqib qolmasligi uchun joy qoldiramiz
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "Back", modifier = Modifier.size(32.dp), tint = Color.White)
                }
                Text(
                    text = "Now Playing",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White.copy(alpha = 0.7f)
                )
                IconButton(onClick = { /* Menyu */ }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.DarkGray)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = currentMediaItem?.mediaMetadata?.title?.toString() ?: "Unknown Title",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = currentMediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown Artist",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = Color(0xFFBB86FC), // Premium Binafsharang
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val sliderPosition = if (duration > 0) currentPosition.toFloat() else 0f
            val sliderMax = if (duration > 0) duration.toFloat() else 100f

            Slider(
                value = sliderPosition,
                onValueChange = { viewModel.seekTo(it) },
                valueRange = 0f..sliderMax,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFBB86FC),
                    activeTrackColor = Color(0xFFBB86FC),
                    inactiveTrackColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(currentPosition), color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
                Text(text = formatTime(duration), color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TINT = Color.White qismi ikonkalarni ko'rinadigan qiladi
                IconButton(onClick = { viewModel.skipToPrevious() }, modifier = Modifier.size(56.dp)) {
                    Icon(imageVector = Icons.Filled.SkipPrevious, contentDescription = "Prev", modifier = Modifier.size(40.dp), tint = Color.White)
                }
                
                IconButton(
                    onClick = { viewModel.playOrPause() },
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFBB86FC))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, 
                        contentDescription = "Play/Pause",
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                }

                IconButton(onClick = { viewModel.skipToNext() }, modifier = Modifier.size(56.dp)) {
                    Icon(imageVector = Icons.Filled.SkipNext, contentDescription = "Next", modifier = Modifier.size(40.dp), tint = Color.White)
                }
            }
        }
    }
}
