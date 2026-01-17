package com.example.comp_3132sef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SchoolEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
}

