package com.workfort.pstuian.ui.checkinlist.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.NavigationButton
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_check_in_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CheckInListScreenContent(
    uiState: CheckInListUiState,
    onUiEvent: (CheckInListUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_check_in_screen),
                navigation = {
                    NavigationButton { onUiEvent(CheckInListUiEvent.OnClickBack) }
                },
            )
        },
    ) {
        when (uiState) {
            is CheckInListUiState.None -> Unit
            is CheckInListUiState.Loading -> {
                CheckInListFullScreenShimmer(modifier = Modifier.fillMaxSize())
            }
            is CheckInListUiState.Error -> {
                CheckInListCenterAction(
                    modifier = Modifier.fillMaxSize(),
                    onClick = { onUiEvent(CheckInListUiEvent.OnClickCheckIn) },
                    content = { AnimatedErrorView() },
                )
            }
            is CheckInListUiState.Content -> {
                CheckInListContentPanel(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}
