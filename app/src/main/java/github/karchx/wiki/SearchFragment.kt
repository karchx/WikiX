/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import github.karchx.wiki.tools.search_engine.SearchEngine
import java.util.*


class SearchFragment : Fragment() {

    private val engine = SearchEngine()
    private var mSearchBtn: Button? = null
    private var mUserRequest: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private class GetListOfPages(val url: String) :
        AsyncTask<String, String, ArrayList<ArrayList<String>>>() {

        override fun doInBackground(vararg params: String?): ArrayList<ArrayList<String>> {
            val engine = SearchEngine()
            val content = engine.getPagesIds(url)
            return engine.getPagesInfo(content!!)
        }

        override fun onPostExecute(response: ArrayList<ArrayList<String>>?) {
            super.onPostExecute(response)
            Log.d("UserRequest", response.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.search_fragment, container, false)

        mSearchBtn = view.findViewById(R.id.searchButton) as Button
        mSearchBtn!!.setOnClickListener {
            mUserRequest = view.findViewById(R.id.editTextUserRequest)
            val userRequest = mUserRequest!!.text.toString()
            val userLang = Locale.getDefault().language

            // Param `request` -- user's request (in search textInput field)
            val url = engine.formUrlPages(userLang, request = userRequest)

            val contentTask = GetListOfPages(url)
            // Start process with getting List of Pages
            contentTask.execute()
        }

        // Return the fragment view/layout
        return view
    }
}