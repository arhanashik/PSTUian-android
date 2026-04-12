package com.workfort.pstuian.view.ui.imageupload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenState
import com.workfort.pstuian.reducer.ui.imageupload.ImageUploadScreenUiEvent
import com.workfort.pstuian.view.ui.common.component.ShowConfirmationDialog
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.img_placeholder_profile
import pstuian.shared.generated.resources.msg_upload_profile_image
import pstuian.shared.generated.resources.txt_browse_gallery
import pstuian.shared.generated.resources.txt_dismiss
import pstuian.shared.generated.resources.txt_upload
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.workfort.pstuian.view.ui.common.component.dashedBorder
import com.workfort.pstuian.view.ui.common.component.rememberImagePickerLauncher


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUploadScreen(
    modifier: Modifier = Modifier,
    screenState: ImageUploadScreenState,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Upload Image") },
                navigationIcon = {
                    IconButton(onClick = { onUiEvent(ImageUploadScreenUiEvent.OnClickBack) }) {
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
    displayState: ImageUploadScreenState.DisplayState,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ImageSelectorView(
            selectedFile = displayState.selectedFileUri,
            onUiEvent = onUiEvent,
        )
        displayState.uploadState.Handle(isPhotoSelected = displayState.selectedFileUri != null)
    }
}

@Composable
private fun ImageSelectorView(
    selectedFile: String?,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        onUiEvent(ImageUploadScreenUiEvent.OnSelectImage(uri))
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .dashedBorder()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
            ) {
                val painter = rememberAsyncImagePainter(
                    model = selectedFile,
                    placeholder = painterResource(Res.drawable.img_placeholder_profile),
                    error = painterResource(Res.drawable.img_placeholder_profile),
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
            Button(
                onClick = {
                    imagePickerLauncher()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                ),
            ) {
                Text(text = stringResource(Res.string.txt_browse_gallery))
            }
        }
        Button(
            modifier = Modifier.size(100.dp),
            onClick = {
                onUiEvent(ImageUploadScreenUiEvent.OnClickUpload)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = if (selectedFile == null) {
                    Color.LightGray
                } else {
                    Color.White
                }
            ),
            enabled = selectedFile != null,
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
private fun ImageUploadScreenState.DisplayState.ImageUploadState.Handle(isPhotoSelected: Boolean) {
    when (this) {
        is ImageUploadScreenState.DisplayState.ImageUploadState.None -> {
            if (isPhotoSelected) {
                Text(
                    text = "Photo Selected",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        is ImageUploadScreenState.DisplayState.ImageUploadState.Uploading -> {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(text = "Uploading... $progress%")
            }
        }
        is ImageUploadScreenState.DisplayState.ImageUploadState.Success -> {
            Text(
                text = "Upload Successful!",
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }
        is ImageUploadScreenState.DisplayState.ImageUploadState.Error -> {
            Text(
                text = "Error: $message",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ImageUploadScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    when (this) {
        is ImageUploadScreenState.DisplayState.MessageState.ConfirmUpload -> {
            ShowConfirmationDialog(
                message = stringResource(Res.string.msg_upload_profile_image),
                confirmButtonText = stringResource(Res.string.txt_upload),
                dismissButtonText = stringResource(Res.string.txt_dismiss),
                onConfirm = {
                    onUiEvent(ImageUploadScreenUiEvent.OnUpload)
                },
                onDismiss = {
                    onUiEvent(ImageUploadScreenUiEvent.MessageConsumed)
                }
            )
        }
        is ImageUploadScreenState.DisplayState.MessageState.Error -> {
            ShowConfirmationDialog(
                message = message,
                confirmButtonText = "OK",
                onConfirm = {
                    onUiEvent(ImageUploadScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ImageUploadScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}
