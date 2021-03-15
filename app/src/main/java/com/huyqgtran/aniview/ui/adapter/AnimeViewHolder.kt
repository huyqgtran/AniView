package com.huyqgtran.aniview.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.huyqgtran.aniview.R
import com.huyqgtran.aniview.data.Anime
import com.huyqgtran.aniview.databinding.AnimeItemBinding

class AnimeViewHolder(
    private val binding: AnimeItemBinding,
    private val glide: RequestManager
): RecyclerView.ViewHolder(binding.root) {
    private var anime: Anime? = null
    fun bind(item: Anime?) {
        anime = item
        anime?.apply{
            image_url?.apply {
                glide.load(this)
                        .apply(RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.ic_insert_photo_black_48dp))
                        .into(binding.plantItemImage)
            }

            val title = "$title\nScore: $score"
            binding.plantItemTitle.text = title
        }
    }

    fun update(anime: Anime?) {
        this.anime = anime
    }

    companion object {
        fun create(parent: ViewGroup, glide: RequestManager): AnimeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AnimeItemBinding.inflate(layoutInflater, parent, false)
            return AnimeViewHolder(binding, glide)
        }
    }
}