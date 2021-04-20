/*
 * Copyright 2021 Alex Syrnikov <pioneer19@post.cz>
 * SPDX-License-Identifier: Apache-2.0
 */

package github.karchx.wiki.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntry::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}