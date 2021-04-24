/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.karchx.wiki.model.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    private val _articlePage = MutableLiveData<ArticlePage>()
    val articlePage: LiveData<ArticlePage> = _articlePage

    fun fetchJsonPage(articleId: Int, lang: String) {
        viewModelScope.launch {
            val articlePage = repository.fetchArticlePage( articleId, lang )
            if( articlePage != null ) {
                val pageStyle = "<link rel=\"stylesheet\" href=\"/w/load.php?lang=en&amp;modules=ext.cite.styles%7Cext.wikimediaBadges%7Cmediawiki.hlist%7Cmediawiki.ui.button%2Cicon%7Cmobile.init.styles%7Cskins.minerva.base.styles%7Cskins.minerva.content.styles%7Cskins.minerva.content.styles.images%7Cskins.minerva.icons.wikimedia%7Cskins.minerva.mainMenu.icons%2Cstyles&amp;only=styles&amp;skin=minerva\"/>\n"
                articlePage.text = pageStyle + articlePage.text
                _articlePage.value = articlePage!!
            }
        }
    }
}
data class ArticlePage(
        var pageId: Int = 0, val lang: String
        , var title: String = "", var text: String = "" )
