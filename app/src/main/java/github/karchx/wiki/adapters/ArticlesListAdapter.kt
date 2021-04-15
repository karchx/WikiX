/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.karchx.wiki.R

class ArticlesListAdapter(
    private val titles: ArrayList<String>,
    private val descriptions: ArrayList<String>,
    private val pageIds: ArrayList<String>
) : RecyclerView.Adapter<ArticlesListAdapter.ArticlesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_article_item, parent, false)
        return ArticlesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val title = titles[position]
        val description = descriptions[position]

        holder.title.text = title
        holder.description.text = description
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.textViewArticleTitle)
        var description: TextView = itemView.findViewById(R.id.textViewArticleDescription)
    }
}