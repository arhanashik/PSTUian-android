package com.workfort.pstuian.ui.deleteaccount.state

sealed interface DeleteAccountNavigationState {
    data object GoBack : DeleteAccountNavigationState
    data object ResetToContactUsScreen : DeleteAccountNavigationState
    data object ResetToHomeScreen : DeleteAccountNavigationState
}
