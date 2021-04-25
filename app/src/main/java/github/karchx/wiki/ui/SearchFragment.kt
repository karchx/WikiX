/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.adapters.ArticlesListAdapter
import github.karchx.wiki.adapters.NewsListAdapter
import github.karchx.wiki.databinding.SearchFragmentBinding
import github.karchx.wiki.listeners.ArticleItemClickListener
import github.karchx.wiki.tools.DrawableManager
import github.karchx.wiki.tools.news_engine.DateParser
import github.karchx.wiki.tools.news_engine.NewsArticleItem
import github.karchx.wiki.tools.news_engine.NewsArticlesItemRecycler
import github.karchx.wiki.tools.news_engine.NewsEngine
import github.karchx.wiki.tools.search_engine.ArticleItem
import github.karchx.wiki.tools.search_engine.MessageBuilder
import github.karchx.wiki.tools.search_engine.SearchEngine
import github.karchx.wiki.ui.helpers.CustomAnimations
import github.karchx.wiki.ui.helpers.ViewExceptions
import github.karchx.wiki.ui.helpers.ViewManager
import github.karchx.wiki.ui.helpers.ViewManager.Companion.hideKeyboard
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private var userLang: String? = null
    private var prefs: SharedPreferences? = null
    private var mProgressBar: ProgressBar? = null
    private var mRequestText: TextView? = null
    private var mNewsFound: TextView? = null
    private var mSearchBtn: Button? = null
    private var mReloadFragmentFab: FloatingActionButton? = null
    private var mUserRequest: EditText? = null
    private var mSearchField: TextInputLayout? = null
    private var mArticlesRecycler: RecyclerView? = null
    private var mNewsArticlesRecycler: RecyclerView? = null
    private var mCardViewNewsItem: CardView? = null
    private var customAnims: CustomAnimations? = null
    private var engine: SearchEngine = SearchEngine()
    private var newsEngine: NewsEngine = NewsEngine()
    private var newsLD: MutableLiveData<ArrayList<NewsArticleItem>> = MutableLiveData()
    private var newsArticles: ArrayList<NewsArticleItem> = ArrayList()
    val ids: ArrayList<Long> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRes()

        customAnims!!.setViewInAnim(mNewsFound!!)
        newsEngine.getNews(newsLD, userLang)
        newsLD.observe(viewLifecycleOwner) { onLoadNews(it) }

        mReloadFragmentFab!!.setOnClickListener { onClickFabReloadFragment() }
        mSearchBtn!!.setOnClickListener { onClickSearchButton(it) }
    }

    private suspend fun showAndCacheNewsArticles(newsArticles: ArrayList<NewsArticleItem>): ArrayList<NewsArticlesItemRecycler> =
        withContext(Dispatchers.Default) {
            val data = ArrayList<NewsArticlesItemRecycler>()

            for (article in newsArticles) {
                mCardViewNewsItem = requireActivity().findViewById(R.id.cardViewNewsItem)
                data.add(
                    NewsArticlesItemRecycler(
                        article.title,
                        article.datePublished,
                        DrawableManager.getDrawable(requireActivity(), article.urlToImage)
                    )
                )
            }

            return@withContext data
        }

    private suspend fun showAndCacheArticles(articles: ArrayList<ArticleItem>) =
        withContext(Dispatchers.Main) {
            setArticlesRecycler(articles)

            mArticlesRecycler!!.addOnItemTouchListener(
                ArticleItemClickListener(
                    requireContext(),
                    mArticlesRecycler!!,
                    object : ArticleItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            customAnims!!.setClickAnim(view)

                            findNavController().navigate(
                                SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                    ids[position],
                                    userLang
                                )
                            )
                        }

                        override fun onItemLongClick(view: View, position: Int) {
                            customAnims!!.setLongClickAnim(view)

                            findNavController().navigate(
                                SearchFragmentDirections.actionSearchFragmentToArticleFragment(
                                    ids[position],
                                    userLang
                                )
                            )
                        }
                    })
            )
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun setNewsArticlesRecycler() {
        val articles = showAndCacheNewsArticles(newsArticles)
        val titles = ArrayList<String>()
        val datesPublishedTime = ArrayList<String>()
        val images = ArrayList<Bitmap>()
        for (article in articles) {
            titles.add(article.title)

            val dateInString = article.datePublishedTime
            val date = DateParser.parseDate(dateInString)

            datesPublishedTime.add(date)
            images.add(article.image)
        }

        // init recycler params
        val layoutManager = GridLayoutManager(context, 1)
        val adapter = NewsListAdapter(titles, datesPublishedTime, images)

        mNewsArticlesRecycler!!.setHasFixedSize(true)
        mNewsArticlesRecycler!!.layoutManager = layoutManager
        mNewsArticlesRecycler!!.adapter = adapter
        customAnims!!.setRecyclerAnim(mNewsArticlesRecycler!!)
    }

    private fun setArticlesRecycler(articles: ArrayList<ArticleItem>) {
        val titles: ArrayList<String> = ArrayList()
        val descriptions: ArrayList<String> = ArrayList()

        for (article in articles) {
            titles.add(article.title)
            descriptions.add(article.description)
            ids.add(article.pageId)
        }

        // init recycler params
        val layoutManager = GridLayoutManager(context, 1)
        val adapter = ArticlesListAdapter(titles, descriptions, ids)
        mArticlesRecycler =
            requireActivity().findViewById(R.id.recyclerViewArticlesList)

        // Show list of articles on display (recycler: title and brief description)
        mArticlesRecycler!!.setHasFixedSize(true)
        mArticlesRecycler!!.layoutManager = layoutManager
        mArticlesRecycler!!.adapter = adapter
        customAnims!!.setRecyclerAnim(mArticlesRecycler!!)
    }

    private fun getArticles(request: String): ArrayList<ArticleItem>? {
        // Param `request` -- user's request (in search textInput field)
        val url = engine.formUrl(userLang!!, request)
        val content = engine.getPagesIds(url)!!
        return engine.getPagesInfo(content)
    }

    private fun foundAnyPages(content: ArrayList<ArticleItem>?): Boolean? {
        if (content == null) {
            // if error while founding
            return null
        }

        // If nothing found
        else if (content.isEmpty()) {
            return false
        }
        // If content found
        return true
    }

    private fun showIncorrectFieldTextError(textField: EditText) {
        val errorMessage = getString(R.string.incorrect_request_error)
        textField.error = errorMessage
        textField.requestFocus()
    }

    private fun showNoConnectionError() {
        val text = getString(R.string.internet_connection_error)
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), text, duration)
        toast.show()
    }

    private fun onLoadNews(articles: ArrayList<NewsArticleItem>) {
        for (article in articles) {
            newsArticles.add(article)
        }
        lifecycleScope.launch { setNewsArticlesRecycler() }
    }

    private fun onClickFabReloadFragment() {
        customAnims!!.setClickAnim(mReloadFragmentFab!!)
        findNavController().navigate(
            R.id.searchFragment,
            arguments,
            NavOptions.Builder()
                .setPopUpTo(R.id.searchFragment, true)
                .build()
        )
    }

    private fun onClickSearchButton(view: View) {
        customAnims!!.setClickAnim(mSearchBtn!!)

        mUserRequest = binding.editTextUserRequest

        val userRequest = mUserRequest!!.text.toString()
        if (!ViewExceptions.isEmptyInputField(userRequest)) {
            val job: Job = GlobalScope.launch(Dispatchers.IO) {

                when {
                    foundAnyPages(getArticles(userRequest)) == null -> {
                        requireActivity().runOnUiThread { showNoConnectionError() }
                    }
                    foundAnyPages(getArticles(userRequest)) == false -> {
                        requireActivity().runOnUiThread {
                            showIncorrectFieldTextError(
                                mUserRequest!!
                            )
                        }
                    }
                    else -> {
                        // Hide all views (only recycler on the screen) for comfortable articles viewing
                        requireActivity().runOnUiThread {
                            view.hideKeyboard()

                            customAnims!!.setViewOutAnim(
                                mSearchBtn!!,
                                mSearchField!!,
                                mUserRequest!!
                            )
                            ViewManager.hideView(
                                mNewsFound!!,
                                mSearchBtn!!,
                                mSearchField!!,
                                mUserRequest!!,
                                mNewsArticlesRecycler!!
                            )

                            mRequestText!!.text =
                                MessageBuilder.getFoundResponseMessage(userLang!!, userRequest)
                            customAnims!!.setViewInAnim(
                                mProgressBar!!,
                                mRequestText!!,
                                mReloadFragmentFab!!
                            )
                            ViewManager.showView(
                                mProgressBar!!,
                                mRequestText!!,
                                mReloadFragmentFab!!
                            )
                        }

                        showAndCacheArticles(getArticles(userRequest)!!)
                        requireActivity().runOnUiThread {
                            ViewManager.hideView(mProgressBar!!)
                        }
                    }
                }
            }
            job.start()
        } else {
            ViewExceptions.showEmptyInputFieldError(requireContext(), mUserRequest!!)
        }
    }

    private fun initRes() {
        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE)
        customAnims = CustomAnimations(requireContext())
        mProgressBar = binding.progressBar
        mRequestText = binding.textViewUserRequest
        mSearchBtn = binding.searchButton
        mNewsFound = binding.textViewNewsFound
        mReloadFragmentFab = binding.fabReloadFragment
        mSearchField = binding.textInputLayoutUserRequest
        mArticlesRecycler = binding.recyclerViewArticlesList
        mNewsArticlesRecycler = binding.recyclerViewNewsArticlesList
        userLang = prefs!!.getString("lang", Locale.getDefault().language)
    }
}
