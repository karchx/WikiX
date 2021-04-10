/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import dagger.hilt.android.AndroidEntryPoint
import github.karchx.wiki.R
import github.karchx.wiki.tools.search_engine.SearchEngine
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val engine = SearchEngine()
    private var mSearchBtn: Button? = null
    private var mUserRequest: EditText? = null
    var _view: View? = null

    private class GetListOfPages(val url: String) :
        AsyncTask<String, String, ArrayList<ArrayList<String>>>() {

        override fun doInBackground(vararg params: String?): ArrayList<ArrayList<String>> {
            val engine = SearchEngine()
            val content = engine.getPagesIds(url)
            return engine.getPagesInfo(content!!)
        }

        override fun onPostExecute(response: ArrayList<ArrayList<String>>?) {
            super.onPostExecute(response)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _view = inflater.inflate(R.layout.search_fragment, container, false)
        initRes(_view!!)

        mSearchBtn!!.setOnClickListener {
            mUserRequest = _view!!.findViewById(R.id.editTextUserRequest)
            val userRequest = mUserRequest!!.text.toString()

            // Param `request` -- user's request (in search textInput field)
            val url = engine.formUrlPages("en", request = userRequest)

            val contentTask = GetListOfPages(url)
            // Start process with getting List of Pages
            contentTask.execute()
        }

        // Return the fragment view/layout
        return _view!!
    }

    private fun initRes(view: View) {
        mSearchBtn = view.findViewById(R.id.searchButton) as Button
    }
}