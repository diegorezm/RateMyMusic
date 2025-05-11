package com.diegorezm.ratemymusic.profile.presentation

sealed interface ProfileScreenActions {
    data object OnFollowClick : ProfileScreenActions
    data object OnUnfollowClick : ProfileScreenActions
    data object OnSettingsClick : ProfileScreenActions
}