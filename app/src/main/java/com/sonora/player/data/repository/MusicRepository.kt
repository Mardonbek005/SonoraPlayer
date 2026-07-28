package com.sonora.player.data.repository

import com.sonora.player.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    /**
     * Telefon xotirasidan musiqalarni skaner qiladi va Local Database'ga saqlaydi.
     */
    suspend fun syncMusicFromDevice()

    /**
     * Ma'lumotlar bazasidan barcha musiqalarni oqim (Flow) orqali oladi.
     */
    fun getAllSongs(): Flow<List<SongEntity>>

    /**
     * Qo'shiqning sevimli holatini o'zgartiradi.
     */
    suspend fun toggleFavorite(mediaId: String, isFavorite: Boolean)
}
