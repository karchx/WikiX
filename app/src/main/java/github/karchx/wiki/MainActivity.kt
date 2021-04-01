/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.databinding.StartFragmentBinding
import github.karchx.wiki.ui.StartFragment
import github.karchx.wiki.ui.StartFragmentDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}