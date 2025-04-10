package com.diegorezm.ratemymusic.reviews.data.repositories

import com.diegorezm.ratemymusic.core.data.RemoteErrorHandler
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewDTO
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewEntityDTO
import com.diegorezm.ratemymusic.reviews.data.dto.ReviewWithProfileDTO
import com.diegorezm.ratemymusic.reviews.data.mappers.toDomain
import com.diegorezm.ratemymusic.reviews.domain.Review
import com.diegorezm.ratemymusic.reviews.domain.ReviewRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class DefaultReviewRepository(
    private val db: Postgrest
) : ReviewRepository {
    private val table = "reviews"

    override suspend fun create(review: ReviewDTO): EmptyResult<DataError> {
        return try {
            db.from(table).insert(review)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun edit(
        reviewId: String,
        review: ReviewDTO
    ): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun remove(reviewId: String): EmptyResult<DataError> {
        return try {
            val review = db.from(table).select {
                filter {
                    eq("id", reviewId)
                }
            }.decodeSingleOrNull<ReviewDTO>()
            if (review == null) {
                Result.Success(Unit)
            } else {
                // TODO: Very silly security concern here, hopefully supabase can help me
                db.from(table).delete {
                    filter { eq("id", reviewId) }
                }
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun getByEntityId(
        entityId: String,
        type: ReviewEntityDTO
    ): Result<List<Review>, DataError> {
        return try {
            val columns = Columns.raw(
                """
            id,
            reviewer_id,
            entity_id,
            entity_type,
            content,
            created_at,
            rating,
            profiles:reviewer_id(uid, email, name, photo_url)
        """.trimIndent()
            )

            val query = db.from(table).select(
                columns
            ) {
                filter {
                    and {
                        eq("entity_id", entityId)
                        eq("entity_type", type.name.lowercase())
                    }
                }

                order("created_at", order = Order.DESCENDING)
            }
            val reviews = query.decodeList<ReviewWithProfileDTO>()
            return Result.Success(reviews.map { it.toDomain() })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun getByUserId(userId: String): Result<List<Review>, DataError> {
        return try {
            val columns = Columns.raw(
                """
            id,
            reviewer_id,
            entity_id,
            entity_type,
            content,
            created_at,
            rating,
            profiles:reviewer_id(uid, email, name, photo_url)
        """.trimIndent()
            )

            val query = db.from(table).select(
                columns
            ) {
                filter {
                    and {
                        eq("reviewer_id", userId)
                    }
                }

                order("created_at", order = Order.DESCENDING)
            }
            val reviews = query.decodeList<ReviewWithProfileDTO>()
            return Result.Success(reviews.map { it.toDomain() })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun getReviewById(reviewId: String): Result<Review, DataError> {
        return try {
            val columns = Columns.raw(
                """
            id,
            reviewer_id,
            entity_id,
            entity_type,
            content,
            created_at,
            rating,
            profiles:reviewer_id(uid, email, name, photo_url)
        """.trimIndent()
            )

            val query = db.from(table).select(
                columns
            ) {
                filter {
                    and {
                        eq("id", reviewId)
                    }
                }

                order("created_at", order = Order.DESCENDING)
            }
            val review = query.decodeSingleOrNull<ReviewWithProfileDTO>()
            if (review == null) {
                return Result.Error(DataError.Local.NOT_FOUND)
            }
            return Result.Success(review.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }
}