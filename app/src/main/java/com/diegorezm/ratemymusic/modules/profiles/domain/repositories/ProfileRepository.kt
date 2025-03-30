package com.diegorezm.ratemymusic.modules.profiles.domain.repositories

import com.diegorezm.ratemymusic.modules.common.PublicException
import com.diegorezm.ratemymusic.modules.profiles.data.models.ProfileDTO
import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.domain.models.Profile
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Count

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
            count(Count.EXACT)
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