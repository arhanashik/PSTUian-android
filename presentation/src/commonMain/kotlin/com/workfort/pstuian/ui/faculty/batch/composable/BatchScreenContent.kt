package com.workfort.pstuian.ui.faculty.batch.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiEvent
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiState

@Composable
internal fun BatchScreenContent(
    uiState: BatchUiState,
    onUiEvent: (BatchUiEvent) -> Unit,
) {
    when (uiState) {
        is BatchUiState.None -> Unit
        is BatchUiState.Loading -> BatchListShimmer()
        is BatchUiState.Content -> BatchContentPanel(uiState, onUiEvent)
        is BatchUiState.Error -> {
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

private fun mockBatches() = listOf(
    Batch(
        id = 1,
        name = "14th Batch",
        title = "Batch 14",
        session = "2022-23",
        facultyId = 1,
        totalStudent = 120,
        registeredStudent = 102,
    ),
    Batch(
        id = 2,
        name = "15th Batch",
        title = "Batch 15",
        session = "2023-24",
        facultyId = 1,
        totalStudent = 130,
        registeredStudent = 98,
    ),
)

@Preview(showBackground = true, name = "Batch - Content")
@Composable
private fun BatchScreenContentPreview() {
    AppTheme {
        BatchScreenContent(
            uiState = BatchUiState.Content(
                isLoading = false,
                batches = mockBatches(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Batch - Dark Content")
@Composable
private fun BatchScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        BatchScreenContent(
            uiState = BatchUiState.Content(batches = mockBatches()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Batch - Loading")
@Composable
private fun BatchScreenContentLoadingPreview() {
    AppTheme {
        BatchScreenContent(
            uiState = BatchUiState.Loading,
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Batch - Loading More")
@Composable
private fun BatchScreenContentLoadingMorePreview() {
    AppTheme {
        BatchScreenContent(
            uiState = BatchUiState.Content(
                batches = mockBatches(),
                isLoading = true,
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Batch - Empty")
@Composable
private fun BatchScreenContentEmptyPreview() {
    AppTheme {
        BatchScreenContent(
            uiState = BatchUiState.Content(
                isLoading = false,
                batches = emptyList(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Batch - Error")
@Composable
private fun BatchScreenContentErrorPreview() {
    AppTheme {
        BatchScreenContent(
            uiState = BatchUiState.Error("Failed to load batches"),
            onUiEvent = {},
        )
    }
}
