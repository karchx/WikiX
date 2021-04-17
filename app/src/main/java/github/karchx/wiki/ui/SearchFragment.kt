/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    private var mRequestText: TextView? = null
    private var mSearchBtn: Button? = null
    private var mUserRequest: EditText? = null
    private var mSearchField: TextInputLayout? = null
    private var mArticlesRecycler: RecyclerView? = null
    private var engine: SearchEngine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        initRes()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)

        mSearchBtn!!.setOnClickListener {
            mUserRequest = binding.editTextUserRequest

            val userRequest = mUserRequest!!.text.toString()
            if (!isEmptyField(userRequest)) {
                val job: Job = GlobalScope.launch(Dispatchers.IO) {
                    // null here returns if in getArticles() was internet connection error
                    if (!getArticles(userRequest).equals(null)) {
                        // Hide all views (only recycler on the screen) for comfortable articles viewing
                        requireActivity().runOnUiThread {
                            hideView(mSearchBtn!!, mSearchField!!, mUserRequest!!)
                            mRequestText!!.text = buildFoundContentMessage(userRequest)
                            showView(mRequestText!!)
                            mRequestText!!.startAnimation(
                                AnimationUtils.loadAnimation(
                                    requireContext(),
                                    android.R.anim.fade_in
                                )
                            )
                            requireView().hideKeyboard()
                        }

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

                // init recycler params
                val layoutManager = GridLayoutManager(context, 1)
                val adapter = ArticlesListAdapter(titles, descriptions, ids)
                val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerViewArticlesList)

                val animId: Int = R.anim.layout_animation
                val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                    requireContext(),
                    animId
                )

                // Show list of articles on display (recycler: title and brief description)
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
                recyclerView.layoutAnimation = animation

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
        val url = engine!!.formUrl(Locale.getDefault().language, request)
        val content = engine!!.getPagesIds(url)!!
        return engine!!.getPagesInfo(content)
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
            view.visibility = View.INVISIBLE
        }
    }

    private fun showView(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initRes() {
        mRequestText = binding.textViewUserRequest
        mSearchBtn = binding.searchButton
        mSearchField = binding.textInputLayoutUserRequest
        mArticlesRecycler = binding.recyclerViewArticlesList
        engine = SearchEngine()
    }

    private fun buildFoundContentMessage(userRequest: String): String {
        val lang = Locale.getDefault().language // en/ru/other language in this format

        var message: String = when {
            lang.equals("en") -> {
                "Found on request:\n"
            }
            lang.equals("ru") -> {
                "Найдено по запросу:\n"
            }
            else -> {
                "Found on request:\n"
            }
        }
        message += userRequest.capitalize(Locale.ROOT)

        return message
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle arrow click
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}