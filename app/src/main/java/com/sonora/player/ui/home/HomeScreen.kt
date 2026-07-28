package com.sonora.player.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sonora.player.ui.components.SongItem
import com.sonora.player.util.PermissionUtils

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var permissionGranted by remember { mutableStateOf(false) }

    // Ruxsatnoma so'rash uchun launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            viewModel.syncMusic()
        }
    }

    // Ekran ochilganda ruxsat so'rashni ishga tushirish
    LaunchedEffect(Unit) {
        permissionLauncher.launch(PermissionUtils.audioPermission)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        if (!permissionGranted) {
            // Ruxsat berilmagan holat UI
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Musiqalarni o'qish uchun ruxsat kerak",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    onClick = { permissionLauncher.launch(PermissionUtils.audioPermission) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Ruxsat berish")
                }
            }
        } else {
            // Musiqalar ro'yxati yoki Yuklanish holati
            if (uiState.isLoading && uiState.songs.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (uiState.songs.isEmpty()) {
                Text(
                    text = "Qurilmada musiqa topilmadi",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Barcha Musiqalar",
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(
                        items = uiState.songs,
                        key = { it.mediaId }
                    ) { song ->
                        SongItem(
                            song = song,
                            onClick = {
                                viewModel.playSong(song, uiState.songs)
                            }
                        )
                    }
                }
            }
        }
    }
}
