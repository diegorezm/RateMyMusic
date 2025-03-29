package com.diegorezm.ratemymusic.modules.reviews.domain.repositories

import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewDTO
import com.diegorezm.ratemymusic.modules.reviews.data.models.ReviewType
import com.diegorezm.ratemymusic.modules.reviews.data.repositories.ReviewsRepository
import com.diegorezm.ratemymusic.modules.reviews.domain.models.Review
import com.diegorezm.ratemymusic.modules.reviews.domain.models.ReviewWithProfile
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class ReviewsRepositoryImpl(
    private val db: Postgrest
) : ReviewsRepository {
    private val table = "reviews"
    override suspend fun create(review: ReviewDTO) {
        db.from(table).insert(review).data
    }

    override suspend fun edit(
        reviewId: String,
        review: ReviewDTO
    ) {
        println("reviewId: $reviewId \n review: $review")
    }

    override suspend fun remove(reviewId: String) {
        db.from(table).delete {
            filter {
                eq("id", reviewId)
            }
        }
    }

    override suspend fun getEntityReviews(
        entityId: String,
        type: ReviewType
    ): List<ReviewWithProfile> {
        val columns = Columns.raw(
            """
            id as reviewId,
            reviewerId,
            entityId,
            entityType,
            content,
            createdAt,
            rating,
            profiles:reviewerId(name as reviewerName, photoUrl as reviewerPhotoUrl)
        """.trimIndent()
        )
        return db.from(table).select(
            columns
        ) {
            filter {
                and {
                    eq("entity_id", entityId)
                    eq("entity_type", type.name)
                }
            }

            order("created_at", order = Order.DESCENDING)
        }
            .decodeList<ReviewWithProfile>()
    }

    override suspend fun getUserReviews(userId: String): List<ReviewWithProfile> {
        val profile = db.from("profiles").select {
            filter {
                eq("uid", userId)
            }
        }.decodeAsOrNull<Profile>()

        if (profile == null) {
            throw PublicException("User not found")
        }

        val query = db.from(table).select {
            filter {
                eq("reviewer_id", userId)
            }
        }.decodeList<Review>()

        val reviewWithProfile = query.map {
            ReviewWithProfile(
                reviewId = it.id,
                reviewerId = profile.uid,
                entityId = it.entityId,
                entityType = it.entityType,
                reviewerName = profile.name,
                reviewerPhotoUrl = profile.photoUrl,
                content = it.content,
                createdAt = it.createdAt,
                rating = it.rating
            )
        }
        return reviewWithProfile
    }


    override suspend fun getReview(reviewId: String): Review {
        val review = db.from(table).select {
            filter {
                eq("id", reviewId)
            }
        }.decodeSingleOrNull<Review>()
        if (review == null) {
            throw PublicException("Review not found")
        }
        return review
    }


}
