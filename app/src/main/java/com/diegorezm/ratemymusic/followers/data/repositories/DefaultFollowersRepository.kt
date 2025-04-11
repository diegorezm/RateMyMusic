package com.diegorezm.ratemymusic.followers.data.repositories

import android.util.Log
import com.diegorezm.ratemymusic.core.data.RemoteErrorHandler
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.followers.data.dto.FollowDTO
import com.diegorezm.ratemymusic.followers.domain.FollowersRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultFollowersRepository(
    private val db: Postgrest
) : FollowersRepository {
    private val table = "followers"


    override suspend fun follow(
        follower: String,
        following: String
    ): EmptyResult<DataError> {
        return try {
            db.from(table).insert(
                FollowDTO(
                    followerId = follower,
                    followingId = following
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("DefaultFollowersRepository", "follow: ${e.message}", e)
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun unfollow(
        follower: String,
        following: String
    ): EmptyResult<DataError> {
        return try {
            db.from(table).delete {
                filter {
                    and {
                        FollowDTO::followerId eq follower
                        FollowDTO::followingId eq following
                    }
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("DefaultFollowersRepository", "unfollow: ${e.message}", e)
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun isFollower(
        follower: String,
        following: String
    ): Flow<Boolean> =
        flow {
            try {
                val result = db.from(table).select {
                    filter {
                        and {
                            FollowDTO::followerId eq follower
                            FollowDTO::followingId eq following
                        }
                    }
                    count(Count.EXACT)
                }.countOrNull()

                emit(result != null && result.toInt() > 0)
            } catch (e: Exception) {
                Log.e("DefaultFollowersRepository", "isFollower: ${e.message}", e)
                emit(false)

            }
        }

    override suspend fun getFollowersCount(userId: String): Result<Int, DataError> {
        return try {
            val result = db.from(table).select {
                filter {
                    FollowDTO::followerId eq userId
                }
                count(Count.EXACT)
            }.countOrNull()
            Result.Success(result?.toInt() ?: 0)
        } catch (e: Exception) {
            Log.e("DefaultFollowersRepository", "getFollowersCount: ${e.message}", e)
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }

    override suspend fun getFollowingCount(userId: String): Result<Int, DataError> {
        return try {
            val result = db.from(table).select {
                filter {
                    FollowDTO::followingId eq userId
                }
                count(Count.EXACT)
            }.countOrNull()
            Result.Success(result?.toInt() ?: 0)
        } catch (e: Exception) {
            Log.e("DefaultFollowersRepository", "getFollowingCount: ${e.message}", e)
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        }
    }
}