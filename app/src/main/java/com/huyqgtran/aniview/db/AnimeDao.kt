package com.huyqgtran.aniview.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huyqgtran.aniview.data.Anime

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Anime>)

    @Query("SELECT * FROM animes WHERE genre = :genre")
    fun animeByGenre(genre: Int): PagingSource<Int, Anime>

    @Query("DELETE FROM animes WHERE genre = :genre")
    suspend fun deleteByGenre(genre: Int)
}