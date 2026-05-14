package com.workfort.pstuian.ui.imageupload.composable

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
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiEvent
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImageUploadScreenContent(
    uiState: ImageUploadUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (ImageUploadUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = "Upload Image",
                navigation = {
                    NavigationButton { onUiEvent(ImageUploadUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is ImageUploadUiState.None -> Unit
            is ImageUploadUiState.Content -> {
                ImageUploadContentPanel(uiState = uiState, onUiEvent = onUiEvent)
            }
        }
    }
}

@Preview(showBackground = true, name = "Upload – initial")
@Composable
private fun ImageUploadScreenContentPreview() {
    AppTheme {
        ImageUploadScreenContent(
            uiState = ImageUploadUiState.Content(),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Upload – photo selected")
@Composable
private fun ImageUploadScreenContentPhotoSelectedPreview() {
    AppTheme {
        ImageUploadScreenContent(
            uiState = ImageUploadUiState.Content(
                selectedFileUri = "https://picsum.photos/400/300",
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Upload – uploading")
@Composable
private fun ImageUploadScreenContentUploadingPreview() {
    AppTheme {
        ImageUploadScreenContent(
            uiState = ImageUploadUiState.Content(
                selectedFileUri = "https://picsum.photos/400/300",
                uploadState = ImageUploadUiState.Content.ImageUploadState.Uploading(progress = 42),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Upload – dark")
@Composable
private fun ImageUploadScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        ImageUploadScreenContent(
            uiState = ImageUploadUiState.Content(
                selectedFileUri = "https://picsum.photos/400/300",
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}