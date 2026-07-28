package com.sonora.player.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonora.player.data.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY dateAdded DESC")
    fun getFavoriteSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs ORDER BY playCount DESC LIMIT 20")
    fun getMostPlayedSongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Query("UPDATE songs SET isFavorite = :isFavorite WHERE mediaId = :mediaId")
    suspend fun updateFavoriteStatus(mediaId: String, isFavorite: Boolean)

    @Query("UPDATE songs SET playCount = playCount + 1 WHERE mediaId = :mediaId")
    suspend fun incrementPlayCount(mediaId: String)
    
    @Query("DELETE FROM songs")
    suspend fun clearAllSongs()
}
