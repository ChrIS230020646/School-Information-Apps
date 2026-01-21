package com.example.comp_3132sef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SchoolEntity::class, FavoriteEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun favoriteDao(): FavoriteDao
}

