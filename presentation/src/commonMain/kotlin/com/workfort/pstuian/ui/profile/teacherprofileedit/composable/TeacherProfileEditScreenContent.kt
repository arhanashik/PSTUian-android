package com.workfort.pstuian.ui.profile.teacherprofileedit.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.title_edit_profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TeacherProfileScreenContent(
    uiState: TeacherProfileEditUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    val title = stringResource(Res.string.title_edit_profile)

    AppScaffold(
        topBar = {
            AppBar(
                title = title,
                navigation = {
                    NavigationButton { onUiEvent(TeacherProfileEditUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is TeacherProfileEditUiState.None -> Unit
            is TeacherProfileEditUiState.Content -> {
                TeacherProfileEditContentPanel(uiState, onUiEvent)

                if (uiState.isLoading) {
                    LoadingOverlay()
                }
            }
        }
    }
}
