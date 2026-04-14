package com.workfort.pstuian.ui.mycheckinlist.state

sealed interface MyCheckInNavigationState {
    data object GoBack : MyCheckInNavigationState
}
