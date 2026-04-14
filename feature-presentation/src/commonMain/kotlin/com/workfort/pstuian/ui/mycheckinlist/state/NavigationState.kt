package com.workfort.pstuian.ui.mycheckinlist.state

sealed interface NavigationState {
    data object GoBack : NavigationState
}
