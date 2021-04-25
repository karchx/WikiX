/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.databinding.SettingsFragmentBinding


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private var langSpinner: Spinner? = null
    private var userLang: String? = null
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        initRes()

        // Return the fragment view/layout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val langAbbreviations = arrayListOf("en", "ru", "es", "it", "zh")

        val index = langAbbreviations.indexOf(userLang)
        langSpinner!!.setSelection(index)

        langSpinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val chosenLang = langAbbreviations[position]
                if (chosenLang != userLang) {
                    prefs!!.edit().putString("lang", chosenLang).apply()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initRes() {
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        userLang = prefs!!.getString("lang", "en")
        langSpinner = binding.spinnerLanguages
    }
}
