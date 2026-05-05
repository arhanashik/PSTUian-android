package com.workfort.pstuian.ui.cvdownload.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.cvdownload.CvDownloadViewModel
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_download_cv_screen
import pstuian.feature_presentation.generated.resources.txt_dismiss

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvDownloadBottomSheet(
    userId: Int,
    userType: UserType,
    url: String,
    onDismiss: () -> Unit,
    viewModel: CvDownloadViewModel = koinViewModel { parametersOf(userId, userType, url) },
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, bottom = 24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TitleTextSmall(text = stringResource(Res.string.label_download_cv_screen))
                IconButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            onDismiss()
                        }
                    },
                ) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.txt_dismiss))
                }
            }
            when (val state = uiState) {
                is CvDownloadUiState.None -> Unit
                is CvDownloadUiState.Content ->
                    CvDownloadContentPanel(
                        modifier = Modifier.padding(top = 8.dp),
                        uiState = state,
                        onUiEvent = viewModel::onUiEvent,
                    )
            }
        }
    }
}
