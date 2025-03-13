package com.diegorezm.ratemymusic.di

import android.content.Context
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepository
import com.diegorezm.ratemymusic.modules.profiles.data_access.ProfileRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

interface AppModule {
    val db: FirebaseFirestore

    val profileRepository: ProfileRepository
}

class AppModuleImpl(context: Context) : AppModule {

    override val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(db)
    }
}