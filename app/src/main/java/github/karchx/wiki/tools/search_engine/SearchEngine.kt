/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.tools.search_engine

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SearchEngine {
    private val client: OkHttpClient = OkHttpClient()
    private val formatter: JsonFormatter = JsonFormatter()

    fun getPagesIds(url: String): String? {
        val content: String?

        val request = Request.Builder()
            .url(url)
            .build()

        // Getting response list of pages with search request keywords
        content = try {
            val response = client.newCall(request).execute()
            response.body!!.string()
            // If can't get response, return empty string
        } catch (e: IOException) {
            ""
        }

        return content
    }

    fun getPagesInfo(content: String): ArrayList<ArrayList<String>>? {
        return if (content.isNotEmpty()) {
            formatter.listOfPages(content)
        } else {
            null
        }
    }

    fun formUrl(lang: String, request: String): String {
        return "https://${lang}.wikipedia.org/w/api.php?action=query&format=json&list=search&utf8=1&srsearch=${request}&srwhat=text"
    }
}