package com.workfort.pstuian.ui.emailverification.state

sealed interface EmailVerificationNavigationState {
    data object GoBack : EmailVerificationNavigationState
}
