package com.sonora.player.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.sonora.player.data.local.dao.SongDao
import com.sonora.player.data.local.entity.SongEntity
import com.sonora.player.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val context: Context,
    private val songDao: SongDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MusicRepository {

    override suspend fun syncMusicFromDevice() {
        withContext(ioDispatcher) {
            val songList = mutableListOf<SongEntity>()
            val collection: Uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            // Bizga qaysi ustunlar (ma'lumotlar) kerakligini belgilaymiz
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DATE_ADDED
            )

            // Faqat musiqa ekanligini va uzunligi 30 soniyadan katta ekanligini tekshiramiz (rington/ovozli xabarlarni filtrlash)
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} >= 30000"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
            )?.use { cursor ->
                // Ustunlar indeksini olamiz
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn) ?: "Unknown Title"
                    val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                    val album = cursor.getString(albumColumn) ?: "Unknown Album"
                    val duration = cursor.getLong(durationColumn)
                    val data = cursor.getString(dataColumn)
                    val albumId = cursor.getLong(albumIdColumn)
                    val dateAdded = cursor.getLong(dateAddedColumn)

                    // Muqova rasm (Album Art) uchun maxsus URI shakllantiramiz
                    val artworkUri = Uri.parse("content://media/external/audio/albumart/$albumId").toString()

                    val song = SongEntity(
                        mediaId = id.toString(),
                        title = title,
                        artist = artist,
                        album = album,
                        duration = duration,
                        data = data,
                        artworkUri = artworkUri,
                        dateAdded = dateAdded
                    )
                    songList.add(song)
                }
            }

            // Olingan ma'lumotlarni Room bazasiga birdaniga (Batch Insert) yozamiz
            if (songList.isNotEmpty()) {
                songDao.insertSongs(songList)
            }
        }
    }

    override fun getAllSongs(): Flow<List<SongEntity>> {
        return songDao.getAllSongs()
    }

    override suspend fun toggleFavorite(mediaId: String, isFavorite: Boolean) {
        withContext(ioDispatcher) {
            songDao.updateFavoriteStatus(mediaId, isFavorite)
        }
    }
}
