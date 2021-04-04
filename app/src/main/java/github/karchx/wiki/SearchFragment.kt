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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val engine = SearchEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        val userLang = Locale.getDefault().language
        // Param `request` -- user's request (in search textInput field)
        val url = engine.formUrl(userLang, request = "Kotlin")
        val contentTask = GetListOfPages(url)
        // Start process with getting List of Pages
        contentTask.execute()
    }

    private class GetListOfPages(val url: String) : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String {
            val engine = SearchEngine()
            val content = engine.getListOfPages(url)
            return content.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // result: List of Pages (xml format)
            // TODO: execute info from xml to structured info (will be shown in recycler)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}