package com.huyqgtran.aniview.api

import android.util.Log
import com.huyqgtran.aniview.data.Anime
import retrofit2.http.Query
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface JikanApi {

    @GET("/v3/search/anime?q=&order_by=score&sort=desc&genre={genre}&page={page}")
    suspend fun getTop(
        @Query("genre") genre: Int? = null,
        @Query("page") page: Int? = null
    ): ListingResponse

    class ListingResponse(val top: List<Anime>)

    companion object {
        private const val BASE_URL = "https://api.jikan.moe/"
        fun create(): JikanApi {
            val logger = HttpLoggingInterceptor { Log.d("API", it) }
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(HttpUrl.parse(BASE_URL)!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JikanApi::class.java)
        }
    }
}