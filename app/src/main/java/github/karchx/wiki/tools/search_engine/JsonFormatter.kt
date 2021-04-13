/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.tools.search_engine

import org.json.JSONObject
import kotlin.collections.ArrayList

class JsonFormatter {

    private val unicodeFormatter: UnicodeFormatter = UnicodeFormatter()

    fun listOfPages(jsonInp: String): ArrayList<ArrayList<String>> {
        val pagesData: ArrayList<ArrayList<String>> = arrayListOf()

        val jsonStr = unicodeFormatter.decode(jsonInp)
        val obj = JSONObject(jsonStr).getJSONObject("query")
        val arrOfPages = obj.getJSONArray("search")

        // get Pages info (will be shown in recycler)
        for (i in 0 until arrOfPages.length()) {
            val pageTitle = arrOfPages.getJSONObject(i).getString("title")
            val pageId = arrOfPages.getJSONObject(i).getString("pageid")
            val pageSnippet = arrOfPages.getJSONObject(i).getString("snippet")

            pagesData.add(arrayListOf(pageTitle, pageId, pageSnippet))
        }

        return pagesData
    }
}