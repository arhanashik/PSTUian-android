package com.workfort.pstuian.ui.signin.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInScreenContent(
    uiState: SignInUiState,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    AppScaffold {
        SignInContentPanel(uiState = uiState, onUiEvent = onUiEvent)
    }
}

@Preview
@Composable
private fun SignInScreenContentPreview() {
    AppTheme {
        SignInScreenContent(
            uiState = SignInUiState(),
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun SignInScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenContent(
            uiState = SignInUiState(),
            onUiEvent = {},
        )
    }
}