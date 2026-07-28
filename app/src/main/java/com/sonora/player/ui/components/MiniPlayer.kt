package com.sonora.player.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sonora.player.ui.player.SharedPlayerViewModel

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    viewModel: SharedPlayerViewModel = hiltViewModel(),
    onNavigateToNowPlaying: () -> Unit
) {
    val currentMediaItem by viewModel.currentMediaItem.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    // Faqat qo'shiq tanlanganda (MediaItem null bo'lmaganda) pastdan chiqib keladi
    AnimatedVisibility(
        visible = currentMediaItem != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 400)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 400)
        ),
        modifier = modifier
            // Telefonning pastki Navigation Bar (Tizim tugmalari) tepasida turishi uchun
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        currentMediaItem?.let { mediaItem ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNavigateToNowPlaying() },
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 8.dp,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Qo'shiq muqovasi (Album Art)
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(mediaItem.mediaMetadata.artworkUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Mini Player Album Art",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Qo'shiq nomi va San'atkor
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = mediaItem.mediaMetadata.title?.toString() ?: "Unknown Title",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = mediaItem.mediaMetadata.artist?.toString() ?: "Unknown Artist",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Boshqaruv tugmalari (Play/Pause va Next)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.playOrPause() }) {
                            // Hozircha standart Android ikonkalari o'rniga Text pleyceholder qo'yamiz.
                            // Keyingi bosqichda chiroyli vektor ikonkalarni yuklaymiz.
                            val playPauseIcon = if (isPlaying) "⏸" else "▶"
                            Text(
                                text = playPauseIcon,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        IconButton(onClick = { viewModel.skipToNext() }) {
                            Text(
                                text = "⏭",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}
