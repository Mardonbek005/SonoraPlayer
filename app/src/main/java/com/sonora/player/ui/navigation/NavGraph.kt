package com.sonora.player.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sonora.player.ui.home.HomeScreen
import com.sonora.player.ui.player.NowPlayingScreen

@Composable
fun SonoraNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 1. Asosiy oyna (Musiqalar ro'yxati)
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen()
        }

        // 2. Asosiy Pleyer oynasi (Now Playing)
        composable(
            route = Screen.NowPlaying.route,
            // Pleyer oynasi pastdan tepaga qarab silliq ochiladi
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                )
            }
        ) {
            NowPlayingScreen(navController = navController)
        }
    }
}

