/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM articles")
    fun getAll(): List<Articles>

    @Query("SELECT * FROM articles WHERE aid = :articleId")
    fun load(articleId: String): Flow<Articles>

    @Insert
    fun insertAll(vararg articles: Articles)

    @Delete
    fun delete(article: Articles)
}