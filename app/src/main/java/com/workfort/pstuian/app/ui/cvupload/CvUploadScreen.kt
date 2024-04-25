package com.workfort.pstuian.app.ui.cvupload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ErrorText
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.component.dashedBorder
import com.workfort.pstuian.util.helper.Toaster


@Composable
fun CvUploadScreen(
    modifier: Modifier = Modifier,
    viewModel: CvUploadViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<CvUploadScreenUiEvent>(CvUploadScreenUiEvent.None)
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
            is CvUploadScreenUiEvent.None -> Unit
            is CvUploadScreenUiEvent.OnSelectCv -> viewModel.onSelectCv(LocalContext.current, uri)
            is CvUploadScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is CvUploadScreenUiEvent.OnClickUpload -> viewModel.onClickUpload()
            is CvUploadScreenUiEvent.OnUpload -> {
                viewModel.messageConsumed()
                viewModel.uploadCv(LocalContext.current)
            }
            is CvUploadScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is CvUploadScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = CvUploadScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: CvUploadScreenState.DisplayState,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = LocalContext.current.getString(R.string.label_upload_cv_screen),
                onClickBack = {
                    onUiEvent(CvUploadScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CvSelectorView(displayState.selectedFile, onUiEvent)
            displayState.cvUploadState.Handle()
        }
    }
}

@Composable
private fun CvSelectorView(
    selectedFile: String,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val pdfPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it?.let { uri ->
            onUiEvent(CvUploadScreenUiEvent.OnSelectCv(uri))
        }
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
            if (selectedFile.isEmpty()) {
                TitleTextSmall(text = "Select a cv to upload")
                Text(
                    text = "Format should be PDF and no more than 2Mb in size",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                Text(
                    text = "Selected file: $selectedFile",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            }
            OutlinedButton(
                onClick = {
                    pdfPicker.launch(arrayOf("application/pdf"))
                },
            ) {
                Text(text = "Browse File")
            }
        }
        Button(
            modifier = Modifier.size(120.dp),
            onClick = {
                onUiEvent(CvUploadScreenUiEvent.OnClickUpload)
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = if (selectedFile.isEmpty()) {
                    Color.LightGray
                } else {
                    Color.White
                }
            ),
            enabled = selectedFile.isNotEmpty(),
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
private fun CvUploadScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun CvUploadScreenState.DisplayState.CvUploadState.Handle() {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (this@Handle) {
            is CvUploadScreenState.DisplayState.CvUploadState.None -> {
                Text(text = "No new upload yet")
            }
            is CvUploadScreenState.DisplayState.CvUploadState.Uploading -> {
                Text(text = "Uploading: $progress%")
            }
            is CvUploadScreenState.DisplayState.CvUploadState.Success -> {
                Text(text = "CV uploaded successfully")
                SelectionContainer {
                    Text(text = url, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                }
                TextButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(url))
                        Toaster.show("Copied to clipboard")
                    },
                ) {
                    Text(text = "Copy Url")
                }
            }
            is CvUploadScreenState.DisplayState.CvUploadState.Error -> {
                ErrorText(text = message)
            }
        }
    }
}

@Composable
private fun CvUploadScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is CvUploadScreenState.DisplayState.MessageState.ConfirmUpload -> {
            ShowConfirmationDialog(
                message = context.getString(R.string.msg_upload_new_cv),
                confirmButtonText = context.getString(R.string.txt_upload),
                onConfirm = {
                    onUiEvent(CvUploadScreenUiEvent.OnUpload)
                },
                onDismiss = {
                    onUiEvent(CvUploadScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CvUploadScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(CvUploadScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(CvUploadScreenUiEvent.MessageConsumed)
                },
            )
        }
    }
}

@Composable
private fun CvUploadScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (CvUploadScreenUiEvent) -> Unit,
) {
    when (this) {
        is CvUploadScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(CvUploadScreenUiEvent.NavigationConsumed)
}