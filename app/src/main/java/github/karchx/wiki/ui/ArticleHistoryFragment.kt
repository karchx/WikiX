/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.databinding.ArticleHistoryFragmentBinding

@AndroidEntryPoint
class ArticleHistoryFragment : Fragment() {

    private var _binding: ArticleHistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleHistoryFragmentBinding.inflate(inflater, container, false)

        val adapter = ArticleHistoryAdapter{ id, lang ->
            val navController = findNavController(this)
            val action = ArticleHistoryFragmentDirections.actionArticleHistoryFragmentToArticleFragment(
                    id.toLong(),lang)
            navController.navigate(action)
        }
        binding.articleHistory.adapter = adapter
        viewModel.getArticlesHistory().observe(viewLifecycleOwner){ articleList ->
            adapter.submitList(articleList)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
