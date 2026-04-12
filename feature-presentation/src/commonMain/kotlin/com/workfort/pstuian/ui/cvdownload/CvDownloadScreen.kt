package com.workfort.pstuian.app.ui.common.ui.cvdownload

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenState
import com.workfort.pstuian.reducer.ui.cvdownload.CvDownloadScreenUiEvent
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.ErrorText
import com.workfort.pstuian.common.component.TitleTextSmall
import com.workfort.pstuian.common.component.dashedBorder
import com.workfort.pstuian.common.component.rememberPdfSaverLauncher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_download_cv_screen
import pstuian.feature_presentation.generated.resources.txt_download


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvDownloadScreen(
    modifier: Modifier = Modifier,
    screenState: CvDownloadScreenState,
    onUiEvent: (CvDownloadScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pdfSaver = rememberPdfSaverLauncher { uri ->
        onUiEvent(CvDownloadScreenUiEvent.OnDownload(uri))
    }
    val displayState = screenState.displayState
    val isDownloadButtonEnabled = displayState.urlToDownload.isNotEmpty() &&
            displayState.cvDownloadState !is CvDownloadScreenState.DisplayState.CvDownloadState.Downloading

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_download_cv_screen),
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
                pdfSaver(displayState.downloadFileName)
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
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
                Text(text = stringResource(Res.string.txt_download), fontSize = 14.sp)
            }
        }
    }
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
