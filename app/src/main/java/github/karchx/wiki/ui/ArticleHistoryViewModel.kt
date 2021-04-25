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
import github.karchx.wiki.model.db.ArticleEntry
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleHistoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    fun getArticlesHistory(): LiveData<List<ArticleEntry>> {
        return repository.getArticlesHistory()
    }
}
