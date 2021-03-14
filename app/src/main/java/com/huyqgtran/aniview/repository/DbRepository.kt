package com.huyqgtran.aniview.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.huyqgtran.aniview.api.JikanApi
import com.huyqgtran.aniview.db.AnimeDb

class DbRepository(private val jikanApi: JikanApi, private val db: AnimeDb) {
    @ExperimentalPagingApi
    fun animesByGenre(genre: Int) = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = PageKeyedRemoteMediator(db, jikanApi, genre)
    ) {
        db.animes().animeByGenre(genre)
    }.flow
}