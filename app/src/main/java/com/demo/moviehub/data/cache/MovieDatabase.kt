package com.demo.moviehub.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.moviehub.data.cache.dao.CachedMovieDao
import com.demo.moviehub.data.cache.entity.CachedMovie

@Database(
    entities = [CachedMovie::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun cachedMovieDao(): CachedMovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_cache.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
