package com.haman.aop_part2_chapter04.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// history 를 저장할 객체 템플릿
// toString, equals, hashcode, copy
@Entity // create 테이블과 동일한 기능
data class History (
    @PrimaryKey val uid : Int?, // primary key
    @ColumnInfo(name = "expression") val expression : String?,
    @ColumnInfo(name = "result") val result : String?
)