package com.workfort.pstuian.ui.home.state

sealed interface MessageState {
    data object SignInNecessary : MessageState
    data object NotificationPermission : MessageState
    data object ClearAllData : MessageState
    data class ClearAllDataFailed(val error: String) : MessageState
}
