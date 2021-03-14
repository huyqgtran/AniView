package com.huyqgtran.aniview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.huyqgtran.aniview.repository.DbRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest


class AnimeViewModel(private val repository: DbRepository): ViewModel() {
    val genre = MutableStateFlow(0)
    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    val animes = genre.flatMapLatest {
        repository.animesByGenre(it)
    }.cachedIn(viewModelScope)
}