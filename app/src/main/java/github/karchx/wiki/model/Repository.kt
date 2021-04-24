/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model

import android.util.Log
import github.karchx.wiki.model.db.AppDao
import github.karchx.wiki.ui.ArticlePage
import io.github.rybalkinsd.kohttp.ext.asString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val appDao: AppDao
) {
    private val localCache = LocalCache(appDao)

    private fun articleJsonUrl(articleId: Int, lang: String): String {
        return "https://${lang}.wikipedia.org/w/api.php?action=parse&format=json&pageid=${articleId}&prop=text&format=json"
    }

    suspend fun fetchArticlePage(articleId: Int, lang: String): ArticlePage? =
        withContext(Dispatchers.IO) {
            try {
                var articlePage = localCache.getArticlePage(articleId, lang)
                if (articlePage != null) {
                    return@withContext articlePage
                } else {
                    val pageUrl: String = articleJsonUrl(articleId, lang)
                    val body = NetUtils.fetchAsync(pageUrl).asString()
                    body ?: return@withContext null

                    val json = JSONObject(body).getJSONObject("parse")
                    articlePage = ArticlePage(
                        json.getInt("pageid"),
                        lang,
                        json.getString("title"),
                        json.getJSONObject("text").getString("*")
                    )
                    localCache.saveArticle(articlePage)

                    return@withContext articlePage
                }
            } catch (ex: Exception) {
                Log.i("fetchArticlePage", "exception: ${ex.message}")
            }
            return@withContext null
        }
}
