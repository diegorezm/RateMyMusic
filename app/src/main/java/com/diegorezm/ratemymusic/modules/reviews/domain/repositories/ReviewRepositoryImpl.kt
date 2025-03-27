package com.diegorezm.ratemymusic.modules.reviews.domain.repositories

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.diegorezm.ratemymusic.modules.profiles.domain.repositories.ProfileRepositoryImpl
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewFilter
import com.diegorezm.ratemymusic.modules.reviews.data.models.toDomain
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ReviewsRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl()
) : ReviewsRepository {


    override suspend fun create(review: ReviewDTO): Result<String> {
        return try {
            val docID =
                "${review.entityType.name.lowercase()}_${review.entityId}_${review.reviewerId}"
            val docRef = db.collection("reviews").document(docID)
            docRef.set(review.toDomain()).await()
            docRef.update("id", docID).await()
            Result.success(docID)
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

    override suspend fun getReviews(filter: ReviewFilter): Result<List<ReviewWithProfile>> {
        return try {
            val query = db.collection("reviews").let {
                when (filter) {
                    is ReviewFilter.ByAlbum -> it.whereEqualTo("entityId", filter.albumId)
                    is ReviewFilter.ByTrack -> it.whereEqualTo("entityId", filter.trackId)
                    is ReviewFilter.ByUser -> it.whereEqualTo("reviewerId", filter.userId)
                }
            }
            // Three O(N) operations? surely nothing bad can come out of it!
            val reviews =
                query
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get().await().documents.mapNotNull {
                        it.toObject(Review::class.java)?.copy(id = it.id)
                    }
            if (reviews.isEmpty()) return Result.success(emptyList())

            val profileIds = reviews.map { it.reviewerId }.distinct()
            val profiles = profileRepository.getProfileByIds(profileIds).getOrNull()

            if (profiles == null) {
                return Result.failure(Exception("Error getting profiles"))
            }

            if (profiles.isEmpty()) return Result.success(emptyList())

            val reviewsWithProfiles = reviews.map { review ->
                ReviewWithProfile(
                    review = review,
                    profile = profiles.find { it.uid == review.reviewerId } ?: Profile(
                        name = "Unknown",
                        uid = "Unknown",
                        photoUrl = null,
                        email = "Unknown",
                    )
                )
            }

            Result.success(reviewsWithProfiles)
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
