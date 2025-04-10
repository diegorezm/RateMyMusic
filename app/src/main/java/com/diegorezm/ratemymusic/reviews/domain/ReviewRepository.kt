package com.diegorezm.ratemymusic.reviews.domain

import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewDTO
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewEntityDTO

interface ReviewRepository {
    suspend fun create(review: ReviewDTO): EmptyResult<DataError>
    suspend fun edit(reviewId: String, review: ReviewDTO): EmptyResult<DataError>
    suspend fun remove(reviewId: String): EmptyResult<DataError>
    suspend fun getByEntityId(
        entityId: String,
        type: ReviewEntityDTO
    ): Result<List<Review>, DataError>

    suspend fun getByUserId(userId: String): Result<List<Review>, DataError>
    
    suspend fun getReviewById(reviewId: String): Result<Review, DataError>

}