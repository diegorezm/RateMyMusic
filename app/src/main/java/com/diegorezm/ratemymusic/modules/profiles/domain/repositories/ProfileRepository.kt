package com.diegorezm.ratemymusic.modules.profiles.domain.repositories

import android.util.Log
import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import io.github.jan.supabase.postgrest.Postgrest

class ProfileRepositoryImpl(
    private val db: Postgrest
) : ProfileRepository {
    private val table = "profiles";

    override suspend fun create(
        dto: ProfileDTO
    ) {
        db.from(table).insert(dto)
    }

    override suspend fun checkIfProfileExists(uid: String): Boolean {
        val profile = db.from(table).select() {
            filter {
                eq("uid", uid)
            }
        }.countOrNull()
        if (profile == null) return false
        return profile > 0
    }

    override suspend fun getProfileById(uid: String): Profile {
        val query = db.from(table).select {
            filter {
                eq("uid", uid)
            }
        }
        val profile = query.decodeSingle<Profile>()
        Log.i("getProfileById", profile.toString())
        return profile
    }

    override suspend fun getProfileByIds(uids: List<String>): List<Profile> {
        val query = db.from(table).select() {
            filter {
                isIn("uid", uids)
            }
        }.decodeList<Profile>()
        if (query.isEmpty()) throw PublicException("Profiles not found")
        return query
    }
}