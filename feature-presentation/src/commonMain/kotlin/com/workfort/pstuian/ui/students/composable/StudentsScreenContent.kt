package com.workfort.pstuian.ui.students.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.students.state.StudentsUiEvent
import com.workfort.pstuian.ui.students.state.StudentsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentsScreenContent(
    uiState: StudentsUiState,
    onUiEvent: (StudentsUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = (uiState as? StudentsUiState.Content)?.title,
                navigation = {
                    NavigationButton { onUiEvent(StudentsUiEvent.BackClicked) }
                },
            )
        },
    ) {
        when (uiState) {
            is StudentsUiState.None -> Unit
            is StudentsUiState.Loading -> LoadingOverlay()
            is StudentsUiState.Content -> StudentsContentPanel(uiState, onUiEvent)
            is StudentsUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedErrorView(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}