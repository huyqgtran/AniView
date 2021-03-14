package com.huyqgtran.aniview.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huyqgtran.aniview.data.GenrePage

@Dao
interface GenrePageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genrePage: GenrePage)

    @Query("SELECT * FROM genres WHERE genre = :genre")
    fun getGenrePage(genre: Int): GenrePage

    @Query("DELETE FROM genres WHERE genre = :genre")
    suspend fun deleteByGenre(genre: Int)
}