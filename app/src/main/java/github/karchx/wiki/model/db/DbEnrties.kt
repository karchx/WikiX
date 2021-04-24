/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverter
import java.util.*

@Entity(tableName = "articles", primaryKeys = ["id", "lang"])
data class ArticleEntry(
    val id: Int,
    val lang: String,
    @ColumnInfo(name = "cache_time")
    val cacheTime: Date,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "text")
    val text: String,
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
