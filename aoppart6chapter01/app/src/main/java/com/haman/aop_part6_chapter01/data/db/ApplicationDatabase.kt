package com.haman.aop_part6_chapter01.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haman.aop_part6_chapter01.data.db.dao.LocationDao
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity

@Database(
    entities = [LocationLatLngEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDatabase.db"
    }

    abstract fun locationDao(): LocationDao
}