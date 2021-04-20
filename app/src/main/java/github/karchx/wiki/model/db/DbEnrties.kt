/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles", primaryKeys = ["id", "lang"])
data class ArticleEntry(
    val id: Int,
    val lang: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val text: String,
)