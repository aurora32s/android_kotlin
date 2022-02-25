package com.haman.aop_part5_chapter02.data.db.dao

import androidx.room.*
import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM ProductEntity WHERE id=:id")
    suspend fun getById(id: Long): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntity: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntityList: List<ProductEntity>)

    @Query("DELETE FROM ProductEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM ProductEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(productEntity: ProductEntity)
}