package com.haman.aop_part3_chapter04.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haman.aop_part3_chapter04.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM REVIEW WHERE id == :id")
    fun getOneReview (id : Int) : Review

    // 똑같은 id 값이 있을 때는 교체하는 형식으로 처리
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview (review : Review)
}