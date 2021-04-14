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

    fun fetchJsonPage(pageUrl: String) {
        viewModelScope.launch {
            repository.fetchArticlePage( _articlePage, pageUrl )
        }
    }
}
data class ArticlePage(var pageId: Int, var title: String, var text: String )