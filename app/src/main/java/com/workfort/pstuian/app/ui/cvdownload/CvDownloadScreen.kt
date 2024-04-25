package com.workfort.pstuian.app.ui.cvdownload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ErrorText
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.component.dashedBorder


@Composable
fun CvDownloadScreen(
    modifier: Modifier = Modifier,
    viewModel: CvDownloadViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<CvDownloadScreenUiEvent>(CvDownloadScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        viewModel.loadInitialValue()
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
            is CvDownloadScreenUiEvent.None -> Unit
            is CvDownloadScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is CvDownloadScreenUiEvent.OnDownload -> viewModel.downloadCv(LocalContext.current, uri)
            is CvDownloadScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = CvDownloadScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: CvDownloadScreenState.DisplayState,
    onUiEvent: (CvDownloadScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pdfPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument(mimeType = "application/pdf")
    ) {
        it?.let { uri ->
            onUiEvent(CvDownloadScreenUiEvent.OnDownload(uri))
        }
    }
    val isDownloadButtonEnabled = displayState.urlToDownload.isNotEmpty() &&
            displayState.cvDownloadState !is CvDownloadScreenState.DisplayState.CvDownloadState.Downloading

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = LocalContext.current.getString(R.string.label_download_cv_screen),
                onClickBack = {
                    onUiEvent(CvDownloadScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DownloadTargetSelectorView(displayState.urlToDownload, isDownloadButtonEnabled) {
                pdfPicker.launch(displayState.downloadFileName)
            }
            displayState.cvDownloadState.Handle()
        }
    }
}

@Composable
private fun DownloadTargetSelectorView(
    urlToDownload: String,
    isDownloadButtonEnabled: Boolean,
    onClickDownload: () -> Unit,
) {
    val context = LocalContext.current

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
        ) {
            if (urlToDownload.isEmpty()) {
                TitleTextSmall(text = "Nothing to download")
            } else {
                Text(text = "Download url")
                Text(
                    text = urlToDownload,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
        Button(
            modifier = Modifier.size(120.dp),
            onClick = { onClickDownload() },
            colors = ButtonDefaults.buttonColors(
                contentColor = if (isDownloadButtonEnabled) {
                    Color.White
                } else {
                    Color.LightGray
                }
            ),
            enabled = isDownloadButtonEnabled,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(painter = painterResource(R.drawable.ic_arrow_down),"")
                Text(text = context.getString(R.string.txt_download), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CvDownloadScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (CvDownloadScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
}

@Composable
private fun CvDownloadScreenState.DisplayState.CvDownloadState.Handle() {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (this@Handle) {
            is CvDownloadScreenState.DisplayState.CvDownloadState.None -> {
                Text(text = "Tap to start download")
            }
            is CvDownloadScreenState.DisplayState.CvDownloadState.Downloading -> {
                Text(text = "Downloading: $progress%")
            }
            is CvDownloadScreenState.DisplayState.CvDownloadState.Success -> {
                SelectionContainer {
                    Text(text = url, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                }
            }
            is CvDownloadScreenState.DisplayState.CvDownloadState.Error -> {
                ErrorText(text = message)
            }
        }
    }
}

@Composable
private fun CvDownloadScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (CvDownloadScreenUiEvent) -> Unit,
) {
    when (this) {
        is CvDownloadScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(CvDownloadScreenUiEvent.NavigationConsumed)
}