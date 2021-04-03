/*
 * Copyright 2021 Andrey Karchevsky <karch.andrus@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.tools.search_engine

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SearchEngine {
    private val client: OkHttpClient = OkHttpClient()

    fun getBasePage(url: String): String? {
        val content: String?

        val request = Request.Builder()
            .url(url)
            .build()

        content = try {
            val response = client.newCall(request).execute()
            response.body!!.string()
        } catch (e: IOException) {
            ""
        }
        return content
    }
}