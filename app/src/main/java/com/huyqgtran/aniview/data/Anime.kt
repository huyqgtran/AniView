package com.huyqgtran.aniview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class Anime(
    @PrimaryKey
    val mal_id: Int,
    val url: String,
    val image_url: String,
    val title: String,
    val airing: Boolean,
    val synopsis: String,
    val type: String,
    val episodes: Int,
    val score: Int,
    val member: Int,
    val rate: String,
    var genre: Int = 0
)