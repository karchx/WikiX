/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import android.util.Log
import github.karchx.wiki.model.db.AppDao
import github.karchx.wiki.model.db.ArticleEntry
import github.karchx.wiki.ui.ArticlePage
import io.github.rybalkinsd.kohttp.ext.asString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class Repository @Inject constructor(
    private val appDao: AppDao
) {
    private fun articleJsonUrl(articleId: Int) : String {
        return "https://wikipedia.org/w/api.php?action=parse&format=json&pageid=${articleId}&prop=text&format=json"
    }
    suspend fun fetchArticlePage( articleId: Int ) : ArticlePage?
            = withContext(Dispatchers.IO){
        try {
            val articleEntry = appDao.getArticle( articleId )
            if ( articleEntry != null ) {
                Log.i( "fetchArticlePage", "got article from Db" )
                return@withContext ArticlePage(articleEntry.id, articleEntry.title, articleEntry.text)
            } else {
                val pageUrl: String = articleJsonUrl(articleId)
                val body = NetUtils.fetchAsync(pageUrl).asString()
                body ?: return@withContext null

                val json = JSONObject(body).getJSONObject("parse")
                val articlePage = ArticlePage(
                        json.getInt("pageid"), json.getString("title"), json.getJSONObject("text").getString("*"))

                Log.i( "fetchArticlePage", "got article from Network" )
                appDao.insertArticle(ArticleEntry(
                        articlePage.pageId, articlePage.title, articlePage.text))
                return@withContext articlePage
            }
        } catch ( ex : Exception )
        {
            Log.i( "fetchArticlePage", "exception: ${ex.message}" )
        }
        return@withContext null
    }
}