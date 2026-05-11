package com.workfort.pstuian.ui.deleteaccount.state

sealed interface DeleteAccountNavigationState {
    data object GoBack : DeleteAccountNavigationState
    data object ResetToHomeScreen : DeleteAccountNavigationState
    data class OpenUrl(val url: String) : DeleteAccountNavigationState
}
