package com.sonora.player.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sonora.player.data.local.dao.SongDao
import com.sonora.player.data.local.entity.PlaylistEntity
import com.sonora.player.data.local.entity.SongEntity

@Database(
    entities = [SongEntity::class, PlaylistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SonoraDatabase : RoomDatabase() {
    abstract val songDao: SongDao
    
    companion object {
        const val DATABASE_NAME = "sonora_player_db"
    }
}
