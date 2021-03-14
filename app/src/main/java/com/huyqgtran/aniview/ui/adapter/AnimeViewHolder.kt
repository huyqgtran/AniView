package com.huyqgtran.aniview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.huyqgtran.aniview.data.Anime
import com.huyqgtran.aniview.databinding.AnimeItemBinding

class AnimeViewHolder(
    private val binding: AnimeItemBinding,
    private val glide: RequestManager
): RecyclerView.ViewHolder(binding.root) {
    private var anime: Anime? = null
    fun bind(anime: Anime?) {

    }

    fun update(anime: Anime?) {

    }

    companion object {
        fun create(parent: ViewGroup, glide: RequestManager): AnimeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AnimeItemBinding.inflate(layoutInflater, parent, false)
            return AnimeViewHolder(binding, glide)
        }
    }
}