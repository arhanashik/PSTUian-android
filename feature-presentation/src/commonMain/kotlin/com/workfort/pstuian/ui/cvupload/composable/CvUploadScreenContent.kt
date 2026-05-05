package com.workfort.pstuian.ui.cvupload.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_upload_cv_screen

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
                title = stringResource(Res.string.label_upload_cv_screen),
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

@Preview(showBackground = true, name = "Upload – initial")
@Composable
private fun CvUploadScreenContentPreview() {
    AppTheme {
        CvUploadScreenContent(
            uiState = CvUploadUiState.Content(),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Upload – file selected")
@Composable
private fun CvUploadScreenContentFileSelectedPreview() {
    AppTheme {
        CvUploadScreenContent(
            uiState = CvUploadUiState.Content(
                selectedFileUri = "document.pdf",
                selectedFileName = "document.pdf",
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Upload – uploading")
@Composable
private fun CvUploadScreenContentUploadingPreview() {
    AppTheme {
        CvUploadScreenContent(
            uiState = CvUploadUiState.Content(
                selectedFileUri = "document.pdf",
                selectedFileName = "document.pdf",
                uploadState = CvUploadUiState.Content.CvUploadState.Uploading(progress = 42),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Upload – dark")
@Composable
private fun CvUploadScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        CvUploadScreenContent(
            uiState = CvUploadUiState.Content(
                selectedFileUri = "document.pdf",
                selectedFileName = "document.pdf",
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}