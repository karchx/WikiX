/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM articles WHERE id = :articleId")
    fun getArticle(articleId: Int): ArticleEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle( article: ArticleEntry )

//    @Query("SELECT * FROM articles")
//    fun getAll(): List<ArticleEntry>
//
//    @Query("SELECT * FROM articles WHERE id = :articleId")
//    fun load(articleId: String): Flow<ArticleEntry>
//
//    @Delete
//    fun delete(article: ArticleEntry)
}