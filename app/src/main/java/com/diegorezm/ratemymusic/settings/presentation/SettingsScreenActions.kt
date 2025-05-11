package com.diegorezm.ratemymusic.settings.presentation

sealed interface SettingsScreenActions {
    data object OnSignOutClick : SettingsScreenActions
    data object OnDeleteAccountClick : SettingsScreenActions
}