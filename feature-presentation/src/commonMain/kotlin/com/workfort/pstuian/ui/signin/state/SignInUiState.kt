package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.UserType

data class SignInUiState(
    val userType: UserType = UserType.STUDENT,
    val isLoading: Boolean = false,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
)
