/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.databinding.ArticleFragmentBinding


@AndroidEntryPoint
class ArticleFragment : Fragment() {

    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by viewModels()
    private val args: ArticleFragmentArgs by navArgs()
    private var mProgressBar: ProgressBar? = null

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

        mProgressBar!!.visibility = View.VISIBLE

        viewModel.articlePage.observe(viewLifecycleOwner){ articlePage ->
            binding.articlePage.loadDataWithBaseURL(
                articleHtmlUrl(articlePage.title, args.lang),
                articlePage.text, "text/html", null, null
            )
        }
        viewModel.fetchJsonPage(articleJsonUrl(args.articleId, args.lang))

        binding.articlePage.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                mProgressBar!!.visibility = View.INVISIBLE
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
    }
}