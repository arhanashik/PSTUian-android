package com.workfort.pstuian.ui.cvupload.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CvUploadScreenContent(
    uiState: CvUploadUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (CvUploadUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = "Upload CV",
                navigation = {
                    NavigationButton { onUiEvent(CvUploadUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is CvUploadUiState.None -> Unit
            is CvUploadUiState.Content -> {
                CvUploadContentPanel(uiState = uiState, onUiEvent = onUiEvent)
            }
        }
    }
}