package com.sonora.player.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [SongEntity] ma'lumotlar bazasida musiqalarni saqlash jadvali.
 */
@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    val mediaId: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val data: String, // Faylning qurilmadagi manzili
    val artworkUri: String?,
    val dateAdded: Long,
    val isFavorite: Boolean = false,
    val playCount: Int = 0
)
