package com.haman.aop_part3_chapter04.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.haman.aop_part3_chapter04.model.History

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HISTORY")
    fun getAll () : List<History>

    @Insert
    fun insertHistory (history : History)

    @Query("DELETE FROM HISTORY WHERE keyword == :keyword")
    fun delete(keyword : String)
}