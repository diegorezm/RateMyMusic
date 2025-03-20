package com.diegorezm.ratemymusic.modules.reviews.domain.repositories

import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.models.toDomain
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewsRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ReviewsRepository {


    override suspend fun create(review: ReviewDTO): Result<String> {
        return try {
            val docRef = db.collection("reviews").add(review).await()
            docRef.update("id", docRef.id).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun edit(reviewId: String, reviewDTO: ReviewDTO): Result<Unit> {
        return try {
            val reviewRef = db.collection("reviews").document(reviewId)
            val review = reviewDTO.toDomain().copy(id = reviewId)

            val snapshot = reviewRef.get().await()
            if (!snapshot.exists()) return Result.failure(Exception("Review not found"))

            reviewRef.set(review.copy(id = reviewId)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun remove(
        reviewId: String,
    ): Result<Unit> {
        return try {
            db.collection("reviews").document(reviewId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReviews(filter: ReviewFilter): Result<List<Review>> {
        return try {
            val query = db.collection("reviews").let {
                when (filter) {
                    is ReviewFilter.ByAlbum -> it.whereEqualTo("entityId", filter.albumId)
                    is ReviewFilter.ByTrack -> it.whereEqualTo("entityId", filter.trackId)
                    is ReviewFilter.ByUser -> it.whereEqualTo("reviewerId", filter.userId)
                }
            }
            // TODO: order by created at
            val reviews =
                query.get().await().documents.mapNotNull {
                    it.toObject(Review::class.java)?.copy(id = it.id)
                }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReview(reviewId: String): Result<Review?> {
        return try {
            val review = db.collection("reviews").document(reviewId).get().await()
                .toObject(Review::class.java)
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
