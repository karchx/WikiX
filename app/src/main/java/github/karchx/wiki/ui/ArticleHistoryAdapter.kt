package github.karchx.wiki.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import github.karchx.wiki.databinding.ArticleHistoryItemBinding
import github.karchx.wiki.model.db.ArticleEntry

class ArticleHistoryAdapter(private val onClickListener: (Int,String)->Unit)
    : ListAdapter<ArticleEntry, ArticleHistoryAdapter.ViewHolder>( DiffCallback() ) {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ViewHolder {
        return ViewHolder.from( parent )
    }

    override fun onBindViewHolder( holder: ViewHolder, position: Int ) {
        val item = getItem( position )
        holder.bind( item, onClickListener )
    }

    class ViewHolder private constructor( private val binding: ArticleHistoryItemBinding )
        : RecyclerView.ViewHolder( binding.root ) {

        fun bind( item: ArticleEntry, onClickListener: (Int,String)->Unit ) {
            binding.articleLang.text = item.lang
            binding.articleTitle.text = item.title
            binding.root.setOnClickListener { onClickListener( item.id, item.lang ); }
        }

        companion object {
            fun from( parent: ViewGroup ): ViewHolder {
                val inflater = LayoutInflater.from( parent.context )
                val binding = ArticleHistoryItemBinding.inflate(
                        inflater, parent, false )
                return ViewHolder( binding )
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleEntry>() {
        override fun areItemsTheSame( oldItem: ArticleEntry, newItem: ArticleEntry ): Boolean {
            return oldItem.id == newItem.id && oldItem.lang == newItem.lang
        }

        override fun areContentsTheSame( oldItem: ArticleEntry, newItem: ArticleEntry ): Boolean {
            return oldItem == newItem
        }
    }
}
