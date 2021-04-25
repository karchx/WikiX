/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.databinding.SearchFragmentBinding
import github.karchx.wiki.databinding.SettingsFragmentBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private var langSpinner: Spinner? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)

        // Return the fragment view/layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initRes() {
    }
}
