package com.diegorezm.ratemymusic.modules.followers.domain.repositories

import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import io.github.jan.supabase.postgrest.Postgrest

class FollowersRepositoryImpl(
    private val db: Postgrest
) : FollowersRepository {
    private val table = "followers"

    override suspend fun follow(payload: FollowDTO) {
        db.from(table).insert(payload)
    }

    override suspend fun unfollow(payload: FollowDTO) {
        db.from(table).delete {
            filter {
                and {
                    eq("following_id", payload.followingId)
                    eq("follower_id", payload.followerId)
                }
            }
        }
    }

    override suspend fun isFollower(
        payload: FollowDTO
    ): Boolean {
        val response = db.from(table).select {
            filter {
                and {
                    eq("following_id", payload.followingId)
                    eq("follower_id", payload.followerId)
                }
            }
        }.countOrNull()

        if (response == null) return false
        return response > 0
    }

    override suspend fun getFollowersCount(userId: String): Int {
        val response = db.from(table).select {
            filter {
                and {
                    eq("following_id", userId)
                }
            }
        }.countOrNull()
        if (response == null) return 0
        return response.toInt()
    }

    override suspend fun getFollowingCount(userId: String): Int {
        val response = db.from(table).select {
            filter {
                and {
                    eq("following_id", userId)
                }
            }
        }.countOrNull()
        if (response == null) return 0
        return response.toInt()
    }

}