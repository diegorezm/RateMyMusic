package com.diegorezm.ratemymusic.presentation.profile.me

import com.diegorezm.ratemymusic.modules.profiles.data.repositories.ProfileRepository
import com.diegorezm.ratemymusic.presentation.profile.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyProfileViewModel(
    profileRepository: ProfileRepository
) : ProfileViewModel(
    userId = Firebase.auth.currentUser?.uid,
    profileRepository = profileRepository
) {
}