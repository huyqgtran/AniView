package com.huyqgtran.aniview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class Anime(
    @PrimaryKey
    val mal_id: Int,
    val url: String?,
    val image_url: String?,
    val title: String,
    val airing: Boolean,
    val synopsis: String,
    val type: String,
    val episodes: Int,
    //TODO
    //score: 0 cause error, fix by using jsonobject and convert later
    val score: Float,
    val member: Int,
    val rated: String,
    var genre: Int = 0
)