package com.sonora.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sonora.player.ui.components.MiniPlayer
import com.sonora.player.ui.navigation.Screen
import com.sonora.player.ui.navigation.SonoraNavGraph
import com.sonora.player.ui.theme.SonoraPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * [MainActivity] ilovaning yagona va asosiy Activity oynasi.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        setContent {
            SonoraPlayerTheme(
                dynamicColor = true,
                amoledMode = false
            ) {
                val navController = rememberNavController()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // NavGraph ilovaning barcha ekranlarini boshqaradi
                    SonoraNavGraph(navController = navController)

                    // Global Mini Player (Har doim ekranning pastki qismida chiqadi)
                    MiniPlayer(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onNavigateToNowPlaying = {
                            navController.navigate(Screen.NowPlaying.route) {
                                // NowPlaying oynasini faqat bitta nusxada ochish uchun
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

