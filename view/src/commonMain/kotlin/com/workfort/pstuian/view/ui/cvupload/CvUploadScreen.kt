package com.workfort.pstuian.view.ui.cvupload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenState
import com.workfort.pstuian.reducer.ui.cvupload.CvUploadScreenUiEvent
import com.workfort.pstuian.view.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.view.ui.common.component.TitleTextSmall
import com.workfort.pstuian.view.ui.common.component.dashedBorder
import com.workfort.pstuian.view.ui.common.component.rememberPdfPickerLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.msg_upload_new_cv
import pstuian.shared.generated.resources.txt_browse_files
import pstuian.shared.generated.resources.txt_dismiss
import pstuian.shared.generated.resources.txt_upload


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvUploadScreen(
    modifier: Modifier = Modifier,
    screenState: CvUploadScreenState,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Upload CV") },
                navigationIcon = {
                    IconButton(onClick = { onUiEvent(CvUploadScreenUiEvent.OnClickBack) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            ScreenContent(
                displayState = screenState.displayState,
                onUiEvent = onUiEvent,
            )
        }
    }

    screenState.displayState.messageState?.Handle(onUiEvent)
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: CvUploadScreenState.DisplayState,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CvSelectorView(
            selectedFileName = displayState.selectedFileName,
            onUiEvent = onUiEvent,
        )
        displayState.cvUploadState.Handle()
    }
}

@Composable
private fun CvSelectorView(
    selectedFileName: String,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    val pdfPickerLauncher = rememberPdfPickerLauncher { uri ->
        onUiEvent(CvUploadScreenUiEvent.OnSelectCv(uri))
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
                onUiEvent(CvUploadScreenUiEvent.OnClickUpload)
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
private fun CvUploadScreenState.DisplayState.CvUploadState.Handle() {
    when (this) {
        is CvUploadScreenState.DisplayState.CvUploadState.None -> Unit
        is CvUploadScreenState.DisplayState.CvUploadState.Uploading -> {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(text = "Uploading... $progress%")
            }
        }
        is CvUploadScreenState.DisplayState.CvUploadState.Success -> {
            Text(
                text = "Upload Successful!",
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }
        is CvUploadScreenState.DisplayState.CvUploadState.Error -> {
            Text(
                text = "Error: $message",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun CvUploadScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    when (this) {
        is CvUploadScreenState.DisplayState.MessageState.ConfirmUpload -> {
            ShowConfirmationDialog(
                message = stringResource(Res.string.msg_upload_new_cv),
                confirmButtonText = stringResource(Res.string.txt_upload),
                dismissButtonText = stringResource(Res.string.txt_dismiss),
                onConfirm = {
                    onUiEvent(CvUploadScreenUiEvent.OnUpload)
                },
                onDismiss = {
                    onUiEvent(CvUploadScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CvUploadScreenState.DisplayState.MessageState.Error -> {
            ShowConfirmationDialog(
                message = message,
                confirmButtonText = "OK",
                onConfirm = {
                    onUiEvent(CvUploadScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(CvUploadScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}
