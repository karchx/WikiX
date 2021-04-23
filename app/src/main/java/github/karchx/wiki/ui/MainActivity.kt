/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.articlePage -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.articleFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.searchPage -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.searchFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settingsPage -> {
                    val navController = findNavController(R.id.nav_host_fragment)
                    navController.navigate(R.id.settingsFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        supportActionBar!!.hide()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.searchPage
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}