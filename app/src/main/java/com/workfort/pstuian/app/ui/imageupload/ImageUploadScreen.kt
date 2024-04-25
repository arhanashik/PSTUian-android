package com.workfort.pstuian.app.ui.imageupload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ErrorText
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.dashedBorder
import com.workfort.pstuian.appconstant.Const


@Composable
fun ImageUploadScreen(
    modifier: Modifier = Modifier,
    viewModel: ImageUploadViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<ImageUploadScreenUiEvent>(ImageUploadScreenUiEvent.None)
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is ImageUploadScreenUiEvent.None -> Unit
            is ImageUploadScreenUiEvent.OnSelectImage -> viewModel.onSelectImage(uri)
            is ImageUploadScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is ImageUploadScreenUiEvent.OnClickUpload -> viewModel.onClickUpload()
            is ImageUploadScreenUiEvent.OnUpload -> {
                viewModel.messageConsumed()
                viewModel.compressAndUploadImage(LocalContext.current)
            }
            is ImageUploadScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is ImageUploadScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = ImageUploadScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: ImageUploadScreenState.DisplayState,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isUploading = displayState.uploadState is
            ImageUploadScreenState.DisplayState.ImageUploadState.Uploading
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = LocalContext.current.getString(R.string.label_upload_image_screen),
                onClickBack = {
                    onUiEvent(ImageUploadScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImageSelectorView(
                selectedFile = displayState.selectedFile,
                isUploading = isUploading,
                onUiEvent = onUiEvent,
            )
            displayState.uploadState.Handle(isPhotoSelected = displayState.selectedFile != null)
        }
    }
}

@Composable
private fun ImageSelectorView(
    selectedFile: Uri?,
    isUploading: Boolean,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.let { uri ->
            onUiEvent(ImageUploadScreenUiEvent.OnSelectImage(uri))
        }
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
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(data = selectedFile)
                        .build(),
                    placeholder = painterResource(R.drawable.img_placeholder_profile),
                    error = painterResource(R.drawable.img_placeholder_profile),
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
                    imagePicker.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                ),
            ) {
                Text(text = "Browse Gallery")
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
            ) {
                Icon(painter = painterResource(R.drawable.ic_arrow_up),"")
                Text(text = context.getString(R.string.txt_upload), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ImageUploadScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun ImageUploadScreenState.DisplayState.ImageUploadState.Handle(
    isPhotoSelected: Boolean
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (this@Handle) {
            is ImageUploadScreenState.DisplayState.ImageUploadState.None -> {
                Text(
                    text = if (isPhotoSelected) {
                        "Tap to start upload"
                    } else {
                        "Select an image to start upload"
                    }
                )
            }
            is ImageUploadScreenState.DisplayState.ImageUploadState.Uploading -> {
                Text(text = "Uploading: $progress%")
            }
            is ImageUploadScreenState.DisplayState.ImageUploadState.Success -> {
                Text(text = "Image uploaded successfully!")
            }
            is ImageUploadScreenState.DisplayState.ImageUploadState.Error -> {
                ErrorText(text = message)
            }
        }
    }
}

@Composable
private fun ImageUploadScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is ImageUploadScreenState.DisplayState.MessageState.ConfirmUpload -> {
            ShowConfirmationDialog(
                message = context.getString(R.string.msg_upload_profile_image),
                confirmButtonText = context.getString(R.string.txt_upload),
                onConfirm = {
                    onUiEvent(ImageUploadScreenUiEvent.OnUpload)
                },
                onDismiss = {
                    onUiEvent(ImageUploadScreenUiEvent.MessageConsumed)
                }
            )
        }
        is ImageUploadScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(ImageUploadScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ImageUploadScreenUiEvent.MessageConsumed)
                },
            )
        }
    }
}

@Composable
private fun ImageUploadScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (ImageUploadScreenUiEvent) -> Unit,
) {
    when (this) {
        is ImageUploadScreenState.NavigationState.GoBack -> {
            url?.let { url ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Const.Key.URL, url)
            }
            navController.popBackStack()
        }
    }
    onUiEvent(ImageUploadScreenUiEvent.NavigationConsumed)
}