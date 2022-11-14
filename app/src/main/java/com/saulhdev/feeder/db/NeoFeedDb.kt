/*
 * This file is part of Neo Feed
 * Copyright (c) 2022   Saul Henriquez <henriquez.saul@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.saulhdev.feeder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

const val ID_UNSET: Long = 0

@Database(
    entities = [
        Feed::class,
        FeedArticle::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NeoFeedDb : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun feedArticleDao(): FeedArticleDao

    companion object {
        @Volatile
        private var instance: NeoFeedDb? = null

        fun getInstance(context: Context): NeoFeedDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NeoFeedDb {
            return Room.databaseBuilder(context, NeoFeedDb::class.java, "NeoFeed")
                .addMigrations(*allMigrations)
                .build()
        }
    }
}

val allMigrations = arrayOf(MIGRATION_1_2)

@Suppress("ClassName")
object MIGRATION_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            ALTER TABLE Feeds ADD COLUMN fullTextByDefault INTEGER NOT NULL DEFAULT 0
            """.trimIndent()
        )
    }
}