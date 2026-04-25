package com.workfort.pstuian.ui.profile.studentprofile.composable

import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.profile.common.composable.ProfileContentPanel
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState

@Composable
fun StudentProfileContentPanel(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    ProfileContentPanel(
        uiState = uiState,
        onUiEvent = onUiEvent,
    ) { expanded, onDismiss, isSignedIn, onEvent ->
        StudentProfileOptionsDropdown(expanded, isSignedIn, onDismiss, onEvent)
    }
}
