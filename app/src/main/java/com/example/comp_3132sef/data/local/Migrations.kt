package com.example.comp_3132sef.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS favorites (" +
                    "englishName TEXT NOT NULL, " +
                    "PRIMARY KEY(englishName)" +
                    ")"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE schools ADD COLUMN chineseName TEXT")
    }
}

