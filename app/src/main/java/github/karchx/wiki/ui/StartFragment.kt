/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.databinding.StartFragmentBinding

@AndroidEntryPoint
class StartFragment : Fragment() {
    private var _binding: StartFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToSettings.setOnClickListener {
            it.findNavController().navigate(StartFragmentDirections.actionStartFragmentToSettingsFragment())
        }
        binding.goToSearch.setOnClickListener {
            it.findNavController().navigate(StartFragmentDirections.actionStartFragmentToSearchFragment())
        }
        binding.goToArticle.setOnClickListener {
            it.findNavController().navigate(StartFragmentDirections.actionStartFragmentToArticleFragment(
                    "Pet_door"
            ))
        }
//        viewModel.text.observe(viewLifecycleOwner) {
//            view.findViewById<TextView>(R.id.testString).text = it
//        }
    }

}