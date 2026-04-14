package com.workfort.pstuian.ui.home.state

sealed interface HomeMessageState {
    data object SignInNecessary : HomeMessageState
    data object NotificationPermission : HomeMessageState
    data object ClearAllData : HomeMessageState
    data class ClearAllDataFailed(val error: String) : HomeMessageState
}
