package com.haman.aop_part6_chapter01.data.repository.review

import com.haman.aop_part6_chapter01.data.entity.impl.ReviewEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantReviewRepository(
    private val ioDispatcher: CoroutineDispatcher
): RestaurantReviewRepository {

    override suspend fun getReviews(restaurantTitle: String) =
        withContext(ioDispatcher){
            return@withContext (0..10).map{
                ReviewEntity(
                    id = it.toLong(),
                    title = "제목 $it",
                    description = "내용 $it",
                    grade = (1 until 5).random()
                )
            }
        }

}