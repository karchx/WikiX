/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.model.db.AppDatabase
import github.karchx.wiki.tools.search_engine.SearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val engine = SearchEngine()
    private var mSearchBtn: Button? = null
    private var mUserRequest: EditText? = null
    private var _view: View? = null
    private var db: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = inflater.inflate(R.layout.search_fragment, container, false)
        initRes(_view!!)

        mSearchBtn!!.setOnClickListener {
            mUserRequest = _view!!.findViewById(R.id.editTextUserRequest)
            val userRequest = mUserRequest!!.text.toString()

            val job: Job = GlobalScope.launch(Dispatchers.IO) {
                getArticles(userRequest)
            }
            job.start()
        }

        // Return the fragment view/layout
        return _view!!
    }

    private fun getArticles(request: String): ArrayList<ArrayList<String>> {
        // Param `request` -- user's request (in search textInput field)
        val url = engine.formUrlPages("en", request)
        val engine = SearchEngine()
        val content = engine.getPagesIds(url)!!
        return engine.getPagesInfo(content)
    }

    private fun initRes(view: View) {
        mSearchBtn = view.findViewById(R.id.searchButton) as Button
    }
}