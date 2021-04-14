/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.adapters.ArticlesListAdapter
import github.karchx.wiki.databinding.SearchFragmentBinding
import github.karchx.wiki.listeners.ArticleItemClickListener
import github.karchx.wiki.tools.search_engine.ArticleItem
import github.karchx.wiki.tools.search_engine.SearchEngine
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val engine = SearchEngine()
    private var mSearchBtn: Button? = null
    private var mUserRequest: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        mSearchBtn = binding.searchButton

        mSearchBtn!!.setOnClickListener {
            mUserRequest = binding.editTextUserRequest
            val userRequest = mUserRequest!!.text.toString()
            if (!isEmptyField(userRequest)) {
                val job: Job = GlobalScope.launch(Dispatchers.IO) {
                    if (!getArticles(userRequest).equals(null)) {
                        showAndCache(getArticles(userRequest))
                    } else {
                        requireActivity().runOnUiThread {
                            showNoConnectionError()
                        }
                    }
                }
                job.start()
            } else {
                showEmptyFieldError(mUserRequest!!)
            }
        }

        // Return the fragment view/layout
        return binding.root
    }

    private suspend fun showAndCache(articles: ArrayList<ArticleItem>) =
        withContext(Dispatchers.Main) {
            if (!foundAnyPages(articles)) {
                showIncorrectFieldTextError(mUserRequest!!)
            } else {
                val titles: ArrayList<String> = ArrayList()
                val pageIds: ArrayList<String> = ArrayList()

                for (article in articles) {
                    titles.add(article.title)
                    // TODO: improve this param (show description instead of page ID)
                    pageIds.add(article.pageId)
                }

                // init recycler
                val layoutManager = GridLayoutManager(context, 1)
                val adapter = ArticlesListAdapter(titles, pageIds)
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
                                findNavController().navigate(
                                    SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                        pageIds[position],
                                        Locale.getDefault().language
                                    )
                                )
                            }

                            override fun onItemLongClick(view: View, position: Int) {
                                findNavController().navigate(
                                    SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                        pageIds[position],
                                        Locale.getDefault().language
                                    )
                                )
                            }
                        })
                )
            }
        }

    private fun getArticles(request: String): ArrayList<ArticleItem> {
        // Param `request` -- user's request (in search textInput field)
        val url = engine.formUrl(Locale.getDefault().language, request)
        val engine = SearchEngine()
        val content = engine.getPagesIds(url)!!
        return engine.getPagesInfo(content)
    }

    private fun showEmptyFieldError(textField: EditText) {
        val errorMessage = "This field cannot be empty"
        textField.error = errorMessage
        // mUserRequest!!.setTextColor(resources.getColor(R.color.some_color, theme))
        textField.requestFocus()
    }

    private fun isEmptyField(fieldContent: String): Boolean {
        return fieldContent.trim { it <= ' ' }.isEmpty()
    }

    private fun foundAnyPages(content: ArrayList<ArticleItem>): Boolean {
        return content.isNotEmpty()
    }

    private fun showIncorrectFieldTextError(textField: EditText) {
        val errorMessage = "Incorrect request. Nothing found"
        textField.error = errorMessage
        // mUserRequest!!.setTextColor(resources.getColor(R.color.some_color, theme))
        textField.requestFocus()
    }

    private fun showNoConnectionError() {
        val text = "No internet, check your connection"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(requireContext(), text, duration)
        toast.show()
    }
}