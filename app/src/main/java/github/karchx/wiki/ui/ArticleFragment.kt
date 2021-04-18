/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.databinding.ArticleFragmentBinding


@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels()
    private val args: ArticleFragmentArgs by navArgs()
    private var mProgressBar: ProgressBar? = null
    private var mReloadFragmentFab: FloatingActionButton? = null
    private var viewAnimIn: Animation? = null
    private var viewAnimOut: Animation? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRes()

        mReloadFragmentFab!!.setOnClickListener {
            // Full fragment recreating
            findNavController().navigate(
                R.id.searchFragment,
                arguments,
                NavOptions.Builder()
                    .setPopUpTo(R.id.searchFragment, true)
                    .build()
            )
        }

        mProgressBar!!.visibility = View.VISIBLE

        viewModel.articlePage.observe(viewLifecycleOwner){ articlePage ->
            if( args.articleId != null ) {
                binding.articlePage.loadDataWithBaseURL(
                    articleHtmlUrl(articlePage.title, args.lang!!),
                    articlePage.text, "text/html", null, null
                )
            }
        }
        if( args.articleId != null ) {
            viewModel.fetchJsonPage(articleJsonUrl(args.articleId!!, args.lang!!))
        }

        binding.articlePage.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                setAnimOut(mProgressBar!!)
                mProgressBar!!.visibility = View.INVISIBLE
                mReloadFragmentFab!!.visibility = View.VISIBLE
                setAnimIn(mReloadFragmentFab!!)
            }
        }
    }

    private fun articleJsonUrl(articleId: String, lang: String) : String {
        return "https://${lang}.wikipedia.org/w/api.php?action=parse&format=json&pageid=${articleId}&prop=text&format=json"
    }
    private fun articleHtmlUrl(articleTitle: String, lang: String) : String {
        return "https://${lang}.wikipedia.org/wiki/${articleTitle}"
    }

    private fun initRes() {
        mProgressBar = binding.progressBar
        mReloadFragmentFab = binding.fabReloadFragment
    }

    private fun setAnimOut(vararg views: View) {
        for (view in views) {
            view.animation = viewAnimOut
        }
    }

    private fun setAnimIn(vararg views: View) {
        for (view in views) {
            view.animation = viewAnimIn
        }
    }
}