package com.workfort.pstuian.ui.cvdownload

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.cvdownload.composable.CvDownloadContentPanel
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadNavigationState
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiEvent
import com.workfort.pstuian.ui.cvdownload.state.CvDownloadUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_download_cv_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CvDownloadScreen(
    viewModel: CvDownloadViewModel,
    navigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(navigation) {
        when (navigation) {
            is CvDownloadNavigationState.GoBack -> {
                navigator.goBack()
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    CvDownloadScreenContent(uiState, viewModel::onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CvDownloadScreenContent(
    uiState: CvDownloadUiState,
    onUiEvent: (CvDownloadUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_download_cv_screen),
                navigation = { onUiEvent(CvDownloadUiEvent.OnClickBack) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        when (uiState) {
            is CvDownloadUiState.None -> Unit
            is CvDownloadUiState.Content -> {
                CvDownloadContentPanel(
                    modifier = Modifier.padding(innerPadding),
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}
