/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.response.observe(viewLifecycleOwner){
            // вот ссылка на WebView из article_fragment.xml layout без findViewById
            // через binding
            binding.articlePage.loadDataWithBaseURL(
                    articleHtmlUrl(args.articleId, args.lang), it, "text/html", null, null)
        }
        viewModel.fetchJsonPage( articleJsonUrl(args.articleId,args.lang) )
    }
    private fun articleJsonUrl(articleId: String, lang: String ) : String {
        Log.i("articleJsonUrl", "id ${articleId}, lang ${lang}")
        return "https://${lang}.wikipedia.org/w/api.php?action=parse&format=json&pageid=${articleId}&prop=text&format=json"
    }
    private fun articleHtmlUrl(articleId: String, lang: String ) : String {
        return "https://${lang}.wikipedia.org/wiki/${articleId}"
    }
}