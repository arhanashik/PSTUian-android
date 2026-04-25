package com.workfort.pstuian.ui.profile.studentprofile.composable

import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.profile.common.composable.ProfileContentPanel
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_student_profile

@Composable
fun StudentProfileContentPanel(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    val title = stringResource(Res.string.txt_student_profile)
    ProfileContentPanel(
        uiState = uiState,
        onUiEvent = onUiEvent,
        screenTitle = title,
    ) { expanded, onDismiss, isSignedIn, onEvent ->
        StudentProfileOptionsDropdown(expanded, isSignedIn, onDismiss, onEvent)
    }
}
