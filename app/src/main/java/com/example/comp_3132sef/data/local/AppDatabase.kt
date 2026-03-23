package com.example.comp_3132sef.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.comp_3132sef.data.local.Filter.SchoolsAddressDao
import com.example.comp_3132sef.data.local.Filter.SchoolsCategoryDao
import com.example.comp_3132sef.data.local.Filter.SchoolsDistrictDao
import com.example.comp_3132sef.data.local.Filter.SchoolsGenderDao
import com.example.comp_3132sef.data.local.Filter.SchoolsReligionDao
import com.example.comp_3132sef.data.local.Filter.SchoolsSessionDao


@Database(
    entities = [SchoolEntity::class, FavoriteEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun schoolsAddressDao(): SchoolsAddressDao
    abstract fun schoolsCategoryDao(): SchoolsCategoryDao
    abstract fun schoolsDistrictDao(): SchoolsDistrictDao
    abstract fun schoolsGenderDao(): SchoolsGenderDao
    abstract fun schoolsReligionDao(): SchoolsReligionDao
    abstract fun schoolsSessionDao(): SchoolsSessionDao
}
