package com.huyqgtran.aniview.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.huyqgtran.aniview.api.JikanApi
import com.huyqgtran.aniview.data.Anime
import com.huyqgtran.aniview.data.GenrePage
import com.huyqgtran.aniview.db.AnimeDb
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PageKeyedRemoteMediator(
    val db: AnimeDb,
    val jikanApi: JikanApi,
    val genreValue: Int
) : RemoteMediator<Int, Anime>() {
    private val animeDao = db.animes()
    private val genrePageDao = db.genrePage()
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Anime>): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = db.withTransaction {
                        genrePageDao.getGenrePage(genreValue)
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if (remoteKey.page == 20) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.page + 1
                }
            }

            var param: Int? = genreValue
            if(genreValue == 0) param = null
            val data = jikanApi.getTop(
                param,
                loadKey
            )

            val items = data.top.map {
                with(it) {
                    Anime(mal_id, url, image_url, title, airing, synopsis, type, episodes, score, member, rate).also {
                        genre = genreValue
                    }
                }
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    animeDao.deleteByGenre(genreValue)
                    genrePageDao.deleteByGenre(genreValue)
                }

                genrePageDao.insert(GenrePage(genreValue, loadKey))
                animeDao.insertAll(items)
            }

            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}