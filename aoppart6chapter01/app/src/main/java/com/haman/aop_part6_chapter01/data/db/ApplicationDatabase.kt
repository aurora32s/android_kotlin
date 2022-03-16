package com.haman.aop_part6_chapter01.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haman.aop_part6_chapter01.data.db.dao.FoodMenuBasketDao
import com.haman.aop_part6_chapter01.data.db.dao.LocationDao
import com.haman.aop_part6_chapter01.data.db.dao.RestaurantDao
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ApplicationDatabase.db"
    }

    abstract fun locationDao(): LocationDao

    abstract fun restaurantDao(): RestaurantDao

    abstract fun foodMenuBasketDao(): FoodMenuBasketDao

}