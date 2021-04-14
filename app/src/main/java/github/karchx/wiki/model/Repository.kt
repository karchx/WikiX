/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import github.karchx.wiki.model.db.AppDao
import github.karchx.wiki.ui.ArticlePage
import io.github.rybalkinsd.kohttp.ext.asString
import org.json.JSONObject
import javax.inject.Inject

class Repository @Inject constructor(
    private val appDao: AppDao
) {
    suspend fun fetchArticlePage( liveData: MutableLiveData<ArticlePage>, pageUrl: String ) {
        try {
            val body = NetUtils.fetchAsync(pageUrl).asString()
            body ?: return

            val json = JSONObject( body ).getJSONObject( "parse" )
            val articlePage = ArticlePage(
                    json.getInt("pageid")
                            ,json.getString("title")
                            ,json.getJSONObject("text").getString("*") )
            liveData.value = articlePage
        } catch ( ex : Exception )
        {
            Log.i( "fetchArticlePage", "error: ${ex.message}" )
        }
    }
}