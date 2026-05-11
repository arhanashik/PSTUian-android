package com.workfort.pstuian.ui.deleteaccount.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiEvent
import com.workfort.pstuian.ui.deleteaccount.state.DeleteAccountUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.txt_delete_account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeleteAccountScreenContent(
    uiState: DeleteAccountUiState,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_delete_account),
                navigation = {
                    NavigationButton { onUiEvent(DeleteAccountUiEvent.BackClicked) }
                },
            )
        },
    ) {
        DeleteAccountContentPanel(uiState, onUiEvent)
    }
}

@Composable
@Preview
private fun DeleteAccountScreenContentPreview() {
    AppTheme {
        DeleteAccountScreenContent(
            uiState = DeleteAccountUiState(),
            onUiEvent = {},
        )
    }
}
