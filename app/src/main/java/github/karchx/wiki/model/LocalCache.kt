package github.karchx.wiki.model

import github.karchx.wiki.model.db.AppDao
import github.karchx.wiki.model.db.ArticleEntry
import github.karchx.wiki.ui.ArticlePage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class LocalCache(private val appDao: AppDao) {
    init {
        GlobalScope.launch {
            deleteOutdatedArticles()
        }
    }

    suspend fun getArticlePage(articleId: Int, lang: String): ArticlePage? {
        val arEntry = appDao.getArticle(articleId, lang)
        if (arEntry == null) {
            return null
        } else {
            if (isOutdatedArticle(arEntry)) {
                deleteOutdatedArticles()
                return null
            } else {
                GlobalScope.launch {
                    appDao.updateArticleAccess(Date())
                }
                return ArticlePage(arEntry.id, arEntry.lang, arEntry.title, arEntry.text)
            }
        }
    }

    suspend fun saveArticle(arPage: ArticlePage) {
        appDao.insertArticle(
            ArticleEntry(arPage.pageId, arPage.lang, Date(), Date(), arPage.title, arPage.text)
        )
    }

    private fun isOutdatedArticle(arEntry: ArticleEntry): Boolean {
        return arEntry.cacheTime.time < (Date().time - CACHE_SECONDS * 1000)
    }

    private suspend fun deleteOutdatedArticles() {
        // remove outdated and almost outdated articles (outdated on 95%)
        val threshold = Date(Date().time - CACHE_SECONDS * (1000 - 50))
        appDao.deleteOutdatedArticles(threshold)
    }

    companion object {
        const val CACHE_SECONDS = 7 * 24 * 3600
    }
}
