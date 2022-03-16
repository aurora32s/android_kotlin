package com.haman.aop_part6_chapter01.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultOrderRepository(
    private val fireStore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
): OrderRepository {
    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>
    ): Result = withContext(ioDispatcher) {
        val result: Result
        val orderMenuData = hashMapOf(
            "restaurantId" to restaurantId,
            "userId" to userId,
            "orderMenuList" to foodMenuList
        )
        result = try {
            fireStore.collection("order")
                .add(orderMenuData)
            Result.Success<Any>()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.Error(exception)
        }
        return@withContext result
    }

    sealed interface Result{
        data class Success<T>(
            val data: T? = null
        ): Result
        data class Error(
            val e: Throwable
        ): Result
    }
}