package com.diegorezm.ratemymusic.profile.data.repositories

import android.util.Log
import com.diegorezm.ratemymusic.core.data.RemoteErrorHandler
import com.diegorezm.ratemymusic.core.domain.DataError
import com.diegorezm.ratemymusic.core.domain.EmptyResult
import com.diegorezm.ratemymusic.core.domain.Result
import com.diegorezm.ratemymusic.profile.data.dto.ProfileDTO
import com.diegorezm.ratemymusic.profile.data.mappers.toDomain
import com.diegorezm.ratemymusic.profile.domain.models.Profile
import com.diegorezm.ratemymusic.profile.domain.repositories.ProfileRepository
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.github.jan.supabase.postgrest.query.Count

class DefaultProfileRepository(
    private val db: Postgrest
) : ProfileRepository {
    private val table = "profiles";

    override suspend fun create(profile: ProfileDTO): EmptyResult<DataError.Remote> {
        return try {
            db.from(table).insert(profile)
            Result.Success(Unit)
        } catch (e: PostgrestRestException) {
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        } catch (e: Exception) {
            Log.e("DefaultProfileRepository", "create: $e")
            Result.Error(RemoteErrorHandler.handleGenericException())
        }
    }

    override suspend fun checkIfProfileExists(uid: String): Result<Boolean, DataError.Remote> {
        try {
            val profile = db.from(table).select() {
                filter {
                    eq("uid", uid)
                }
                count(Count.EXACT)
            }.countOrNull()
            return Result.Success(profile != null && profile > 0)
        } catch (e: PostgrestRestException) {
            return Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        } catch (e: Exception) {
            Log.e("DefaultProfileRepository", "checkIfProfileExists: $e")
            return Result.Error(RemoteErrorHandler.handleGenericException())
        }
    }


    override suspend fun getProfileById(uid: String): Result<Profile, DataError.Remote> {
        return try {
            val query = db.from(table).select {
                filter {
                    eq("uid", uid)
                }
            }
            val profile = query.decodeSingle<ProfileDTO>()
            Result.Success(profile.toDomain())
        } catch (e: PostgrestRestException) {
            Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        } catch (e: Exception) {
            Log.e("DefaultProfileRepository", "getProfileById: $e")
            Result.Error(RemoteErrorHandler.handleGenericException())
        }
    }

    override suspend fun getProfileByIds(uids: List<String>): Result<List<Profile>, DataError.Remote> {
        try {
            val query = db.from(table).select {
                filter {
                    isIn("uid", uids)
                }
            }
            val profiles = query.decodeList<ProfileDTO>()
            return Result.Success(profiles.map { it.toDomain() })
        } catch (e: PostgrestRestException) {
            return Result.Error(RemoteErrorHandler.handlePostgrestException(e))
        } catch (e: Exception) {
            Log.e("DefaultProfileRepository", "getProfileByIds: $e")
            return Result.Error(RemoteErrorHandler.handleGenericException())
        }
    }

}