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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.common.composable.dashedBorder
import com.workfort.pstuian.common.composable.rememberPdfPickerLauncher
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
            selectedFileName = uiState.selectedFileName,
            onUiEvent = onUiEvent,
        )
        CvUploadStatusView(uiState)
    }
}

@Composable
private fun CvSelectorView(
    selectedFileName: String,
    onUiEvent: (CvUploadUiEvent) -> Unit,
) {
    val pdfPickerLauncher = rememberPdfPickerLauncher { uri ->
        // Assuming we can get the filename from URI or it's handled elsewhere
        // For now, using a placeholder for filename if not provided
        onUiEvent(CvUploadUiEvent.CvSelected(uri, "selected_file.pdf"))
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
            if (selectedFileName.isEmpty()) {
                TitleTextSmall(text = "Select a cv to upload")
                Text(
                    text = "Format should be PDF and no more than 2Mb in size",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                Text(
                    text = "Selected file: $selectedFileName",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            }
            OutlinedButton(
                onClick = {
                    pdfPickerLauncher()
                },
            ) {
                Text(text = stringResource(Res.string.txt_browse_files))
            }
        }
        Button(
            modifier = Modifier.size(100.dp),
            onClick = {
                onUiEvent(CvUploadUiEvent.UploadClicked)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = if (selectedFileName.isEmpty()) {
                    Color.LightGray
                } else {
                    Color.White
                }
            ),
            enabled = selectedFileName.isNotEmpty(),
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
private fun CvUploadStatusView(uiState: CvUploadUiState.Content) {
    when {
        uiState.progress in 1..99 -> {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(text = "Uploading... ${uiState.progress}%")
            }
        }
        uiState.uploadResult != null -> {
            Text(
                text = uiState.uploadResult,
                color = if (uiState.isUploadSuccess) Color.Green else Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
