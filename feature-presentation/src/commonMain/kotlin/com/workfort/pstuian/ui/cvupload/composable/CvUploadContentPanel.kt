package com.workfort.pstuian.ui.cvupload.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.composable.dashedBorder
import com.workfort.pstuian.ui.common.composable.rememberPdfPickerLauncher
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_browse_files
import pstuian.feature_presentation.generated.resources.txt_upload

@Composable
internal fun CvUploadContentPanel(
    modifier: Modifier = Modifier,
    uiState: CvUploadUiState.Content,
    onUiEvent: (CvUploadUiEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CvSelectorView(
            selectedFileUri = uiState.selectedFileUri,
            onUiEvent = onUiEvent,
        )
        CvUploadStatusView(
            uploadState = uiState.uploadState,
            isCvSelected = uiState.selectedFileUri != null,
        )
    }
}

@Composable
private fun CvSelectorView(
    selectedFileUri: String?,
    onUiEvent: (CvUploadUiEvent) -> Unit,
) {
    val pdfPickerLauncher = rememberPdfPickerLauncher { fileUri ->
        onUiEvent(CvUploadUiEvent.CvSelected(fileUri))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(16.dp)
                .dashedBorder(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (selectedFileUri.isNullOrEmpty()) {
                TitleTextSmall(text = "Select a cv to upload")
                Text(
                    text = "Format should be PDF and no more than 2Mb in size",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                Text(
                    text = "Selected file: $selectedFileUri",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            }
            OutlinedButton(onClick = { pdfPickerLauncher() }) {
                Text(text = stringResource(Res.string.txt_browse_files))
            }
        }
        Button(
            modifier = Modifier.size(100.dp),
            onClick = {
                selectedFileUri?.let { onUiEvent(CvUploadUiEvent.UploadClicked(selectedFileUri)) }
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = if (selectedFileUri.isNullOrEmpty()) {
                    Color.LightGray
                } else {
                    Color.White
                }
            ),
            enabled = !selectedFileUri.isNullOrEmpty(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text(text = stringResource(Res.string.txt_upload))
            }
        }
    }
}

@Composable
private fun CvUploadStatusView(
    uploadState: CvUploadUiState.Content.CvUploadState,
    isCvSelected: Boolean,
) {
    when (uploadState) {
        is CvUploadUiState.Content.CvUploadState.None -> {
            if (isCvSelected) {
                Text(
                    text = "Cv Selected",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        is CvUploadUiState.Content.CvUploadState.Uploading -> {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(text = "Uploading... ${uploadState.progress}%")
            }
        }
        is CvUploadUiState.Content.CvUploadState.Success -> {
            Text(
                text = "Upload Successful!",
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }
        is CvUploadUiState.Content.CvUploadState.Error -> {
            Text(
                text = "Error: ${uploadState.message}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
