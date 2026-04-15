package com.workfort.pstuian.ui.imageupload.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.workfort.pstuian.common.composable.dashedBorder
import com.workfort.pstuian.common.composable.rememberImagePickerLauncher
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiEvent
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.img_placeholder_profile
import pstuian.feature_presentation.generated.resources.txt_browse_gallery
import pstuian.feature_presentation.generated.resources.txt_upload

@Composable
internal fun ImageUploadContentPanel(
    modifier: Modifier = Modifier,
    uiState: ImageUploadUiState.Content,
    onUiEvent: (ImageUploadUiEvent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        ImageSelectorView(
            selectedFile = uiState.selectedFileUri,
            onUiEvent = onUiEvent,
        )
        ImageUploadStatusView(
            uploadState = uiState.uploadState,
            isPhotoSelected = uiState.selectedFileUri != null,
        )
    }
}

@Composable
private fun ImageSelectorView(
    selectedFile: String?,
    onUiEvent: (ImageUploadUiEvent) -> Unit,
) {
    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        onUiEvent(ImageUploadUiEvent.ImageSelected(uri))
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
                onUiEvent(ImageUploadUiEvent.UploadClicked)
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
private fun ImageUploadStatusView(
    uploadState: ImageUploadUiState.Content.ImageUploadState,
    isPhotoSelected: Boolean,
) {
    when (uploadState) {
        is ImageUploadUiState.Content.ImageUploadState.None -> {
            if (isPhotoSelected) {
                Text(
                    text = "Photo Selected",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        is ImageUploadUiState.Content.ImageUploadState.Uploading -> {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Text(text = "Uploading... ${uploadState.progress}%")
            }
        }
        is ImageUploadUiState.Content.ImageUploadState.Success -> {
            Text(
                text = "Upload Successful!",
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }
        is ImageUploadUiState.Content.ImageUploadState.Error -> {
            Text(
                text = "Error: ${uploadState.message}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
