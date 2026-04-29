package com.workfort.pstuian.ui.profile.studentprofileedit.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.title_edit_profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentProfileScreenContent(
    uiState: StudentProfileEditUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    val title = stringResource(Res.string.title_edit_profile)

    AppScaffold(
        topBar = {
            AppBar(
                title = title,
                navigation = {
                    NavigationButton { onUiEvent(StudentProfileEditUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            is StudentProfileEditUiState.None -> Unit
            is StudentProfileEditUiState.Content -> {
                StudentProfileEditContentPanel(uiState, onUiEvent)

                if (uiState.isLoading) {
                    LoadingOverlay()
                }
            }
        }
    }
}