package com.workfort.pstuian.ui.settings.state

sealed interface SettingsNavigationState {
    data object GoBack : SettingsNavigationState
    data object ResetToRoot : SettingsNavigationState
}
