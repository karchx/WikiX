/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.adapters.ArticlesListAdapter
import github.karchx.wiki.listeners.ArticleItemClickListener
import github.karchx.wiki.tools.search_engine.SearchEngine
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val engine = SearchEngine()
    private var mSearchBtn: Button? = null
    private var mUserRequest: EditText? = null
    private var _view: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = inflater.inflate(R.layout.search_fragment, container, false)
        initRes(_view!!)

        mSearchBtn!!.setOnClickListener {
            mUserRequest = _view!!.findViewById(R.id.editTextUserRequest)
            val userRequest = mUserRequest!!.text.toString()

            val job: Job = GlobalScope.launch(Dispatchers.IO) {
                showAndCache(getArticles(userRequest))
            }
            job.start()
        }

        // Return the fragment view/layout
        return _view!!
    }

    private suspend fun showAndCache(articles: ArrayList<ArrayList<String>>) =
        withContext(Dispatchers.Main) {
            val titles: ArrayList<String> = ArrayList()
            val descriptions: ArrayList<String> = ArrayList()

            for (article in articles) {
                titles.add(article[1])
                descriptions.add(article[2])
            }

            // init recycler
            val layoutManager = GridLayoutManager(context, 1)
            val adapter = ArticlesListAdapter(titles, descriptions)
            val recyclerView =
                requireActivity().findViewById<RecyclerView>(R.id.recyclerViewArticlesList)

            // Show list of articles on display (recycler)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter

            recyclerView.addOnItemTouchListener(
                ArticleItemClickListener(
                    requireContext(),
                    recyclerView,
                    object : ArticleItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            Log.d("Clicked item: ", position.toString())
                            findNavController().navigate(
                                SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                    titles[position],
                                    Locale.getDefault().language
                                )
                            )
                        }

                        override fun onItemLongClick(view: View, position: Int) {
                            Log.d("Long Clicked item: ", position.toString())
                            findNavController().navigate(
                                SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                    titles[position],
                                    Locale.getDefault().language
                                )
                            )
                        }
                    })
            )
        }

    private fun getArticles(request: String): ArrayList<ArrayList<String>> {
        // Param `request` -- user's request (in search textInput field)
        val url = engine.formUrl(Locale.getDefault().language, request)
        val engine = SearchEngine()
        val content = engine.getPagesIds(url)!!
        return engine.getPagesInfo(content)
    }

    private fun initRes(view: View) {
        mSearchBtn = view.findViewById(R.id.searchButton) as Button
    }
}