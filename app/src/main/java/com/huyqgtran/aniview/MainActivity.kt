package com.huyqgtran.aniview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.huyqgtran.aniview.api.JikanApi
import com.huyqgtran.aniview.databinding.ActivityMainBinding
import com.huyqgtran.aniview.db.AnimeDb
import com.huyqgtran.aniview.repository.DbRepository
import com.huyqgtran.aniview.ui.AnimeViewModel
import com.huyqgtran.aniview.ui.adapter.AnimeAdapter
import com.huyqgtran.aniview.ui.adapter.AnimesLoadStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.collect
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
        private set

    private val viewModel: AnimeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repo = DbRepository(JikanApi.create(), AnimeDb.create(this@MainActivity))
                return AnimeViewModel(repo) as T
            }

        }
    }

    private lateinit var adapter: AnimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initSwipeToRefresh()
        initEnterGenre()
    }

    private fun initAdapter() {
        val glide = Glide.with(this)
        adapter = AnimeAdapter(glide)
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = AnimesLoadStateAdapter(adapter),
            footer = AnimesLoadStateAdapter(adapter)
        )

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.animes.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    private fun initSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun initEnterGenre() {
        binding.input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                enterGenre()
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                enterGenre()
                true
            } else {
                false
            }
        }
    }

    private fun enterGenre() {
        viewModel.genre.value = binding.input.text.toString().toInt()
    }
}