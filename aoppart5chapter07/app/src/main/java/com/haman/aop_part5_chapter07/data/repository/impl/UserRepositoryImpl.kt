package com.haman.aop_part5_chapter07.data.repository.impl

import com.haman.aop_part5_chapter07.data.api.UserApi
import com.haman.aop_part5_chapter07.data.preference.PreferenceManager
import com.haman.aop_part5_chapter07.data.repository.UserRepository
import com.haman.aop_part5_chapter07.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
): UserRepository {

    override suspend fun getUser(): User? = withContext(dispatcher){
        preferenceManager.getString(KEY_USER_ID)?.let { User(it) }
    }

    override suspend fun saveUser(user: User) = withContext(dispatcher){
        val newUser = userApi.saveUser(user)
        preferenceManager.putString(KEY_USER_ID, newUser.id!!)
    }

    companion object {
        private const val KEY_USER_ID = "KEY_USER_ID"
    }

}