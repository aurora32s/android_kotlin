package com.haman.aop_part2_chapter04

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haman.aop_part2_chapter04.dao.HistoryDao
import com.haman.aop_part2_chapter04.model.History

// entities : table 생성
// version : 해당 어플리케이션의 DB 설계 버전
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract  fun historyDao() : HistoryDao
}