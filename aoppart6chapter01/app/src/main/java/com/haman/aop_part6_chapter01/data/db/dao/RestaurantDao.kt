package com.haman.aop_part6_chapter01.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity

@Dao
interface RestaurantDao {

    @Query("SELECT * FROm RestaurantEntity WHERE restaurantTitle=:title")
    suspend fun get(title: String): RestaurantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurantEntity: RestaurantEntity)

    @Query("DELETE FROM RestaurantEntity WHERE restaurantTitle=:title")
    suspend fun delete(title: String)

    @Query("DELETE FROM RestaurantEntity")
    suspend fun deleteAll()

}