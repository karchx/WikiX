/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface AppDao {
    @Query("SELECT * FROM articles WHERE id = :articleId AND lang = :lang")
    suspend fun getArticle(articleId: Int, lang: String): ArticleEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntry)

    @Query("DELETE FROM articles WHERE cache_time < :date")
    suspend fun deleteOutdatedArticles(date: Date)

//    @Query( "SELECT id, lang FROM articles")
//    suspend fun getArticlesDebug(): List<DebugArticle>

//    @Query("SELECT * FROM articles")
//    suspend fun getArticles(): List<ArticleEntry>

//    @Query("SELECT * FROM articles WHERE id = :articleId")
//    fun load(articleId: String): Flow<ArticleEntry>
//
//    @Delete
//    fun delete(article: ArticleEntry)
}
