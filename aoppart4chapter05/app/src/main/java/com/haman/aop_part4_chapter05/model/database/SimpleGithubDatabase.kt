package com.haman.aop_part4_chapter05.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haman.aop_part4_chapter05.model.dao.RepositoryDao
import com.haman.aop_part4_chapter05.model.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase: RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao
}