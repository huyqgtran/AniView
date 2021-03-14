package com.huyqgtran.aniview.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huyqgtran.aniview.data.Anime
import com.huyqgtran.aniview.data.GenrePage

@Database(entities = [Anime::class, GenrePage::class], version = 1)
abstract class AnimeDb : RoomDatabase() {
    companion object {
        fun create(context: Context): AnimeDb {
            return Room.databaseBuilder(context, AnimeDb::class.java, "reddit.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun animes(): AnimeDao
    abstract fun genrePage(): GenrePageDao
}