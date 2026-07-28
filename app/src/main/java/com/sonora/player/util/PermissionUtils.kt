package com.sonora.player.util

import android.Manifest
import android.os.Build

object PermissionUtils {
    /**
     * Android versiyasiga qarab musiqa o'qish uchun kerakli ruxsatnomani qaytaradi.
     * Android 13 (API 33) va undan yuqori versiyalarda READ_MEDIA_AUDIO,
     * undan past versiyalarda esa READ_EXTERNAL_STORAGE talab qilinadi.
     */
    val audioPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
}
