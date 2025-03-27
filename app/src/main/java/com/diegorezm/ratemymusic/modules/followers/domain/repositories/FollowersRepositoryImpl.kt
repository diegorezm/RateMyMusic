package com.diegorezm.ratemymusic.modules.followers.domain.repositories

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.followers.data.models.FollowDTO
import com.diegorezm.ratemymusic.modules.followers.data.models.toFollower
import com.diegorezm.ratemymusic.modules.followers.data.models.toFollowing
import com.diegorezm.ratemymusic.modules.followers.data.repositories.FollowersRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FollowersRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : FollowersRepository {
    override suspend fun follow(payload: FollowDTO): Result<Unit> {
        return try {
            val batch = db.batch()

            val followedDocumentRef =
                db.collection("profiles").document(payload.followedId).collection("followers")
                    .document(payload.followerId)

            val followerDocumentRef =
                db.collection("profiles").document(payload.followerId).collection("following")
                    .document(payload.followedId)

            if (followedDocumentRef.get().await().exists() || followerDocumentRef.get().await()
                    .exists()
            ) {
                Result.success(Unit)
            }

            batch.set(followedDocumentRef, payload.toFollower())
            batch.set(followerDocumentRef, payload.toFollowing())

            batch.commit().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("FollowersRepositoryImpl", "follow: ${e.message}")
            Result.failure(PublicException("Error trying to follow user with id ${payload.followedId}"))
        }
    }

    override suspend fun unfollow(payload: FollowDTO): Result<Unit> {
        return try {
            val followedDocumentRef =
                db.collection("profiles").document(payload.followedId).collection("followers")
                    .document(payload.followerId)

            val followerDocumentRef =
                db.collection("profiles").document(payload.followerId).collection("following")
                    .document(payload.followedId)

            if (followedDocumentRef.get().await().exists() || followerDocumentRef.get().await()
                    .exists()
            ) {
                val batch = db.batch()
                batch.delete(followedDocumentRef)
                batch.delete(followerDocumentRef)
                batch.commit().await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("FollowersRepositoryImpl", "unfollow: ${e.message}")
            Result.failure(PublicException("Error trying to unfollow user with id ${payload.followedId}"))
        }
    }

    override suspend fun isFollower(followingId: String, followerId: String): Result<Boolean> {
        return try {
            val followedDocumentRef =
                db.collection("profiles").document(followingId).collection("followers")
            val isFollower = followedDocumentRef.document(followerId).get().await().exists()
            Result.success(isFollower)
        } catch (e: Exception) {
            Log.e("FollowersRepositoryImpl", "isFollower: ${e.message}")
            Result.failure(PublicException("Error trying to check if user with id $followingId is a follower of user with id $followerId"))
        }
    }

    override suspend fun getFollowersCount(userId: String): Result<Int> {
        return try {
            val followersCount = db.collection("profiles").document(userId).collection("followers")
                .get().await().size()
            Result.success(followersCount)
        } catch (e: Exception) {
            Log.e("FollowersRepositoryImpl", "getFollowersCount: ${e.message}")
            Result.failure(PublicException("Error trying to get followers count of user with id $userId"))
        }
    }

    override suspend fun getFollowingCount(userId: String): Result<Int> {
        return try {
            val followingCount = db.collection("profiles").document(userId).collection("following")
                .get().await().size()
            Result.success(followingCount)
        } catch (e: Exception) {
            Log.e("FollowersRepositoryImpl", "getFollowingCount: ${e.message}")
            Result.failure(PublicException("Error trying to get following count of user with id $userId"))
        }
    }

    override suspend fun getFollowers(userId: String): Result<List<String>> {
        return try {
            val followers = db.collection("profiles").document(userId).collection("followers")
                .get().await().documents.map { it.id }
            Result.success(followers)
        } catch (e: Exception) {
            Log.e("FollowersRepositoryImpl", "getFollowers: ${e.message}")
            Result.failure(PublicException("Error trying to get followers of user with id $userId"))
        }
    }

    override suspend fun getFollowing(userId: String): Result<List<String>> {
        return try {
            val following = db.collection("profiles").document(userId).collection("following")
                .get().await().documents.map { it.id }
            Result.success(following)
        } catch (e: Exception) {
            Log.e("FollowersRepositoryImpl", "getFollowing: ${e.message}")
            Result.failure(PublicException("Error trying to get following of user with id $userId"))
        }
    }
}