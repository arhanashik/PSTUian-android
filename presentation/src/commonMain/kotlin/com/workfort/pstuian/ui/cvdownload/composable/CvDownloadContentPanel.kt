package com.workfort.pstuian.ui.cvdownload.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.ui.common.composable.ErrorText
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.common.composable.dashedBorder
import com.workfort.pstuian.ui.common.composable.rememberPdfSaverLauncher
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiEvent
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.txt_download

@Composable
internal fun CvDownloadContentPanel(
    modifier: Modifier = Modifier,
    uiState: CvDownloadUiState.Content,
    onUiEvent: (CvDownloadUiEvent) -> Unit,
) {
    val pdfSaver = rememberPdfSaverLauncher { uri ->
        onUiEvent(CvDownloadUiEvent.OnSaveDestinationChosen(uri))
    }

    val isDownloadButtonEnabled = uiState.urlToDownload.isNotEmpty() &&
            (uiState.progress == 0 || uiState.progress == 100)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DownloadTargetSelectorView(uiState.urlToDownload, isDownloadButtonEnabled) {
            pdfSaver(uiState.downloadFileName)
        }
        CvDownloadStatusView(uiState)
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
            onClick = onClickDownload,
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
private fun CvDownloadStatusView(uiState: CvDownloadUiState.Content) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when {
            uiState.progress in 1..99 -> {
                Text(text = "Downloading: ${uiState.progress}%")
            }
            uiState.downloadResult != null -> {
                if (uiState.isDownloadSuccess) {
                    SelectionContainer {
                        Text(
                            text = uiState.downloadResult,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    ErrorText(text = uiState.downloadResult)
                }
            }
            else -> {
                Text(text = "Tap to start download")
            }
        }
    }
}
