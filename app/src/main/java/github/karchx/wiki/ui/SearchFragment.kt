/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
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
    private var mSearchField: TextInputLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        mSearchBtn = binding.searchButton
        mSearchField = binding.textInputLayoutUserRequest

        mSearchBtn!!.setOnClickListener {
            mUserRequest = binding.editTextUserRequest

            // Hide all views (only recycler on the screen) for comfortable articles viewing
            hideView(mSearchBtn!!, mSearchField!!, mUserRequest!!)
            requireView().hideKeyboard()

            val userRequest = mUserRequest!!.text.toString()
            if (!isEmptyField(userRequest)) {
                val job: Job = GlobalScope.launch(Dispatchers.IO) {
                    // null here returns if in getArticles() was internet connection error
                    if (!getArticles(userRequest).equals(null)) {
                        showAndCache(getArticles(userRequest))
                    } else {
                        // Handle here internet connection error;
                        // display toast with description in UI thread
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
                val descriptions: ArrayList<String> = ArrayList()
                val ids: ArrayList<String> = ArrayList()

                for (article in articles) {
                    titles.add(article.title)
                    descriptions.add(article.description)
                    ids.add(article.pageId)
                }

                // init recycler
                val layoutManager = GridLayoutManager(context, 1)
                val adapter = ArticlesListAdapter(titles, descriptions, ids)
                val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerViewArticlesList)

                // Show list of articles on display (recycler: title and brief description)
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
                val itemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
                itemDecoration.setDrawable(getDrawable(recyclerView.context,R.drawable.recycle_view_divider)!!)
                recyclerView.addItemDecoration(itemDecoration)

                recyclerView.addOnItemTouchListener(
                    ArticleItemClickListener(
                        requireContext(),
                        recyclerView,
                        object : ArticleItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                findNavController().navigate(
                                    SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                        ids[position],
                                        Locale.getDefault().language
                                    )
                                )
                            }

                            override fun onItemLongClick(view: View, position: Int) {
                                findNavController().navigate(
                                    SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                        ids[position],
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
        val errorMessage = getString(R.string.empty_field_error)
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
        val errorMessage = getString(R.string.incorrect_request_error)
        textField.error = errorMessage
        // mUserRequest!!.setTextColor(resources.getColor(R.color.some_color, theme))
        textField.requestFocus()
    }

    private fun showNoConnectionError() {
        val text = getString(R.string.internet_connection_error)
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), text, duration)
        toast.show()
    }

    private fun hideView(vararg views: View) {
        for (view in views) {
            view.visibility = if (requireView().visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}