package com.huyqgtran.aniview.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenrePage(
    @PrimaryKey
    val genre: Int,
    val page: Int)