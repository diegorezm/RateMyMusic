package com.diegorezm.ratemymusic.modules.reviews.domain.repositories

import com.diegorezm.ratemymusic.modules.reviews.data.models.EntityType
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewsRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ReviewsRepository {

    override suspend fun create(review: ReviewDTO): Result<Unit> {
        return try {
            val reviewId =
                "${review.entityType.name.lowercase()}_${review.entityId}_${review.reviewerId}"
            db.collection("reviews")
                .document(reviewId)
                .set(review)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun edit(review: ReviewDTO): Result<Unit> {
        return try {
            val reviewId =
                "${review.entityType.name.lowercase()}_${review.entityId}_${review.reviewerId}"
            val reviewRef = db.collection("reviews").document(reviewId)

            val snapshot = reviewRef.get().await()
            if (!snapshot.exists()) return Result.failure(Exception("Review not found"))

            reviewRef.set(review).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun remove(
        entityId: String,
        entityType: EntityType,
        userId: String
    ): Result<Unit> {
        return try {
            val reviewId = "${entityType.name.lowercase()}_${entityId}_${userId}"
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
            val reviews =
                query.get().await().documents.mapNotNull { it.toObject(Review::class.java) }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
