package com.haman.aop_part5_chapter07.data.repository

import com.haman.aop_part5_chapter07.domain.model.Review

/**
 * 댓글 관리 repository
 */
interface ReviewRepository {
    suspend fun getLatestReview(movieId: String): Review?
}