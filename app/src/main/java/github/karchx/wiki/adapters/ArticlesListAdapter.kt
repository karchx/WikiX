package github.karchx.wiki.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import github.karchx.wiki.R

class ArticlesListAdapter(private val titles: ArrayList<String>, private val descriptions: ArrayList<String>) :  RecyclerView.Adapter<ArticlesListAdapter.ArticlesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.articles_layout, parent, false)
        return ArticlesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val title = titles[position]
        val desc = descriptions[position]

        holder.title.text = title
        holder.desc.text = desc
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.textViewArticleTitle)
        var desc: TextView = itemView.findViewById(R.id.textViewArticleDescription)
    }
}