/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.karchx.wiki.R

class NewsListAdapter(
    private val titles: ArrayList<String>,
    private val datesPublishedTime: ArrayList<String>,
    private val images: ArrayList<Bitmap>
) : RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_article_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val title = titles[position]
        val datePublishedAt = datesPublishedTime[position]
        val image = images[position]

        holder.title.text = title
        holder.datePublishedTime.text = datePublishedAt
        holder.image.setImageBitmap(image)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.textViewNewsArticleTitle)
        var datePublishedTime: TextView = itemView.findViewById(R.id.textViewNewsArticlePublishedDate)
        var image: ImageView = itemView.findViewById(R.id.imageViewNewsArticle)
    }
}