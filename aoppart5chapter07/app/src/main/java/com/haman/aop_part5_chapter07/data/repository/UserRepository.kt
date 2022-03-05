package com.haman.aop_part5_chapter07.data.repository

import com.haman.aop_part5_chapter07.domain.model.User

interface UserRepository {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
}