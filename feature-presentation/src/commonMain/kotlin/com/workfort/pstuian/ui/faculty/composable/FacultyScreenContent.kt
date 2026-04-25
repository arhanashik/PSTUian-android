package com.workfort.pstuian.ui.faculty.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacultyScreenContent(
    uiState: FacultyUiState,
    onUiEvent: (FacultyUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = uiState.title,
                navigation = { onUiEvent(FacultyUiEvent.BackClicked) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        when (uiState) {
            is FacultyUiState.None -> Unit
            is FacultyUiState.Content -> FacultyContentPanel(uiState, onUiEvent)
        }
    }
}