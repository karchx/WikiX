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
import androidx.fragment.app.Fragment
import github.karchx.wiki.tools.search_engine.SearchEngine
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    private val engine = SearchEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userLang = Locale.getDefault().language

        // Param `request` -- user's request (in search textInput field)
        val url = engine.formUrlPages(userLang, request = "Kotlin")

        val contentTask = GetListOfPages(url)
        // Start process with getting List of Pages
        contentTask.execute()
    }

    private class GetListOfPages(val url: String) :
        AsyncTask<String, String, ArrayList<ArrayList<String>>>() {

        override fun doInBackground(vararg params: String?): ArrayList<ArrayList<String>> {
            val engine = SearchEngine()
            val content = engine.getPagesIds(url)
            return engine.getPagesInfo(content!!)
        }

        override fun onPostExecute(result: ArrayList<ArrayList<String>>?) {
            super.onPostExecute(result)
            Log.d("ResultOfGetting", result.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false)
    }
}