package com.haman.aop_part2_chapter04.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.haman.aop_part2_chapter04.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory (history : History)

    @Query("DELETE FROM history")
    fun deleteAll()

    @Delete
    fun deleteHistory (history : History)

    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
    fun findByResult (result : String) : List<History>
}