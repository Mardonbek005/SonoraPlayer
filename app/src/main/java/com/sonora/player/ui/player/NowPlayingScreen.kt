package com.sonora.player.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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

    // Holatlar ViewModel'dan olinadi (boshqa qo'shiqqa o'tsa ham o'chib qolmaydi)
    val isShuffleEnabled by viewModel.isShuffleEnabled.collectAsState()
    val isRepeatEnabled by viewModel.isRepeatEnabled.collectAsState()

    val goldColor = Color(0xFFFFD700)
    val darkGold = Color(0xFFB8860B)
    val blackBg = Color(0xFF030303)

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
            .background(blackBg)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
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
                    Icon(Icons.Filled.KeyboardArrowDown, "Back", modifier = Modifier.size(32.dp), tint = goldColor)
                }
                Text(
                    text = "S O N O R A",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 3.sp),
                    color = goldColor
                )
                IconButton(onClick = { /* Menyu */ }) {
                    Icon(Icons.Filled.MoreVert, "Menu", tint = goldColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val artworkUri = currentMediaItem?.mediaMetadata?.artworkUri
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, Brush.linearGradient(listOf(goldColor, darkGold, blackBg)), RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(artworkUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Large Album Art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = currentMediaItem?.mediaMetadata?.title?.toString() ?: "Unknown Title",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = currentMediaItem?.mediaMetadata?.artist?.toString() ?: "Unknown Artist",
                style = MaterialTheme.typography.bodyMedium,
                color = goldColor.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { viewModel.toggleShuffle() }) {
                    Icon(
                        Icons.Filled.Shuffle, 
                        "Shuffle", 
                        tint = if (isShuffleEnabled) goldColor else goldColor.copy(alpha = 0.4f), 
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                    val sliderPosition = if (duration > 0) currentPosition.toFloat() else 0f
                    val sliderMax = if (duration > 0) duration.toFloat() else 100f

                    Slider(
                        value = sliderPosition,
                        onValueChange = { viewModel.seekTo(it) },
                        valueRange = 0f..sliderMax,
                        colors = SliderDefaults.colors(
                            thumbColor = goldColor,
                            activeTrackColor = goldColor,
                            inactiveTrackColor = Color(0xFF333333)
                        )
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(formatTime(currentPosition), color = goldColor.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text(formatTime(duration), color = goldColor.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                }

                IconButton(onClick = { viewModel.toggleRepeat() }) {
                    Icon(
                        Icons.Filled.Repeat, 
                        "Repeat", 
                        tint = if (isRepeatEnabled) goldColor else goldColor.copy(alpha = 0.4f), 
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GoldCircularButton(icon = Icons.Filled.VolumeUp, label = "VOLUME", size = 56.dp, iconSize = 28.dp) { 
                    viewModel.toggleMute()
                }
                
                GoldCircularButton(icon = Icons.Filled.SkipPrevious, label = "PREVIOUS", size = 64.dp, iconSize = 32.dp) { viewModel.skipToPrevious() }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(darkGold.copy(alpha = 0.3f), blackBg)))
                            .border(3.dp, goldColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { viewModel.playOrPause() }, modifier = Modifier.size(80.dp)) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, 
                                contentDescription = "Play/Pause",
                                tint = goldColor,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "SONORA", color = goldColor, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }

                GoldCircularButton(icon = Icons.Filled.SkipNext, label = "NEXT", size = 64.dp, iconSize = 32.dp) { viewModel.skipToNext() }
                
                GoldCircularButton(icon = Icons.Filled.MoreHoriz, label = "MORE", size = 56.dp, iconSize = 28.dp) { /* More */ }
            }
        }
    }
}

@Composable
fun GoldCircularButton(icon: ImageVector, label: String, size: Dp, iconSize: Dp, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(size)
                .border(2.dp, Color(0xFFFFD700).copy(alpha = 0.6f), CircleShape)
                .background(Color(0xFF0A0A0A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClick) {
                Icon(icon, contentDescription = label, tint = Color(0xFFFFD700), modifier = Modifier.size(iconSize))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = Color(0xFFFFD700).copy(alpha = 0.7f), fontSize = 8.sp, letterSpacing = 1.sp)
    }
}
