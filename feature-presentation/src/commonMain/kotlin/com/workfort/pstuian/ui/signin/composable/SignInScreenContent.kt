package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.common.theme.ApplySystemBarColors
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInScreenContent(
    uiState: SignInUiState,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    // Status bar = green header, navigation bar = white (or dark) form section. Icon contrast is
    // derived from the luminance of each surface so it stays correct across both themes.
    // On Android this is implemented via native window APIs (status/nav bar colors). On iOS
    // there is no equivalent API, so the screen instead draws its green and white sections
    // edge-to-edge behind the safe areas; the status bar and home indicator then blend with
    // the content underneath. The inner content (skip button, form, etc.) is padded out of the
    // safe area inside SignInScreenUi.
    val statusBarColor = MaterialTheme.colorScheme.primary
    val navigationBarColor = MaterialTheme.colorScheme.surface
    ApplySystemBarColors(
        statusBarColor = statusBarColor,
        statusBarDarkIcons = statusBarColor.luminance() > 0.5f,
        navigationBarColor = navigationBarColor,
        navigationBarDarkIcons = navigationBarColor.luminance() > 0.5f,
    )

    AppScaffold(contentWindowInsets = WindowInsets(0)) {
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

@Preview
@Composable
private fun SignInScreenUiPreview() {
    AppTheme {
        SignInScreenUi()
    }
}

@Preview
@Composable
private fun SignInScreenUiDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi()
    }
}

@Preview
@Composable
private fun SignUpPanelPreview() {
    AppTheme {
        SignInScreenUi(initialPanel = AuthPanel.SignUp)
    }
}

@Preview
@Composable
private fun SignUpPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi(initialPanel = AuthPanel.SignUp)
    }
}

@Preview
@Composable
private fun ForgotPasswordPanelPreview() {
    AppTheme {
        SignInScreenUi(initialPanel = AuthPanel.ForgotPassword)
    }
}

@Preview
@Composable
private fun ForgotPasswordPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi(initialPanel = AuthPanel.ForgotPassword)
    }
}

@Preview
@Composable
private fun EmailVerificationPanelPreview() {
    AppTheme {
        SignInScreenUi(initialPanel = AuthPanel.EmailVerification)
    }
}

@Preview
@Composable
private fun EmailVerificationPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SignInScreenUi(initialPanel = AuthPanel.EmailVerification)
    }
}