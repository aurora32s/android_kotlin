package com.haman.aop_part5_chapter07.data.api

import com.haman.aop_part5_chapter07.domain.model.User

interface UserApi {
    suspend fun saveUser(user: User): User
}