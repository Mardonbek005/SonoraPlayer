package com.sonora.player.ui.navigation

/**
 * [Screen] ilovadagi barcha oynalar (ekranlar) manzillarini o'z ichiga oladi.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object NowPlaying : Screen("now_playing_screen")
    object Playlist : Screen("playlist_screen")
    object Settings : Screen("settings_screen")
    object DeveloperMode : Screen("developer_mode_screen") // Yashirin ekran
}
