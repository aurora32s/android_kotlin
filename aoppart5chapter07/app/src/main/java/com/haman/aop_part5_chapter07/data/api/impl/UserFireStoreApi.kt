package com.haman.aop_part5_chapter07.data.api.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.haman.aop_part5_chapter07.data.api.UserApi
import com.haman.aop_part5_chapter07.domain.model.User
import kotlinx.coroutines.tasks.await

class UserFireStoreApi(
    private val fireStore: FirebaseFirestore
): UserApi {

    override suspend fun saveUser(user: User): User =
        fireStore.collection("users")
            .add(user)
            .await()
            .let { User(it.id) }

}