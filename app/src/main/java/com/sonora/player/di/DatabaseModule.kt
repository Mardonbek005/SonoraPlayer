package com.sonora.player.di

import android.app.Application
import androidx.room.Room
import com.sonora.player.data.local.SonoraDatabase
import com.sonora.player.data.local.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSonoraDatabase(app: Application): SonoraDatabase {
        return Room.databaseBuilder(
            app,
            SonoraDatabase::class.java,
            SonoraDatabase.DATABASE_NAME
        )
        // Katta ma'lumotlar bazasi RAM'ni qiynamasligi va I/O thread'da ishlashi kafolatlanadi
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideSongDao(database: SonoraDatabase): SongDao {
        return database.songDao
    }
}
