package com.sonora.player

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * SonoraApp - Ilovaning asosiy Application klassi.
 * 
 * @HiltAndroidApp annotatsiyasi ilovada Dagger-Hilt orqali Dependency Injection
 * ishlatilishini bildiradi. Bu orqali ViewModel, Repository va Database kabi
 * resurslar avtomatik boshqariladi.
 */
@HiltAndroidApp
class SonoraApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // TODO: Bu yerda Logger (masalan Timber), Analytics yoki
        // ThemeManager kabi dastlabki sozlamalar initsializatsiya qilinadi.
        setupDeveloperTools()
    }

    private fun setupDeveloperTools() {
        // Kelajakda Developer Mode funksiyalari (Log Viewer, Crash Reporter)
        // uchun maxsus tayyorgarlik kodlari shu yerda bo'ladi.
    }
}
