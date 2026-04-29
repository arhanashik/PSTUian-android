package com.workfort.pstuian.ui.changepassword.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.common.theme.ApplySystemBarColors
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_change_password

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChangePasswordScreenContent(
    uiState: ChangePasswordUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    val statusBarColor = MaterialTheme.colorScheme.primary
    val navigationBarColor = MaterialTheme.colorScheme.surface
    ApplySystemBarColors(
        statusBarColor = statusBarColor,
        statusBarDarkIcons = statusBarColor.luminance() > 0.5f,
        navigationBarColor = navigationBarColor,
        navigationBarDarkIcons = navigationBarColor.luminance() > 0.5f,
    )

    val changePasswordTitle = stringResource(Res.string.txt_change_password)

    AppScaffold(
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    ) {
        when (uiState) {
            is ChangePasswordUiState.None -> Unit
            is ChangePasswordUiState.ChangePassword -> {
                ChangePasswordContentPanel(
                    uiState = uiState,
                    changePasswordHeaderTitle = changePasswordTitle,
                    onUiEvent = onUiEvent,
                )
            }
            is ChangePasswordUiState.ResetPassword -> {
                ChangePasswordContentPanel(
                    uiState = uiState,
                    changePasswordHeaderTitle = changePasswordTitle,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChangePasswordPanelPreview() {
    AppTheme {
        ChangePasswordScreenContent(
            uiState = ChangePasswordUiState.ChangePassword(
                input = ChangePasswordInput.INITIAL,
                validationError = ChangePasswordInputError.INITIAL,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun ChangePasswordPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        ChangePasswordScreenContent(
            uiState = ChangePasswordUiState.ChangePassword(
                input = ChangePasswordInput.INITIAL,
                validationError = ChangePasswordInputError.INITIAL,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun ChangePasswordWithValidationErrorsPreview() {
    AppTheme {
        ChangePasswordScreenContent(
            uiState = ChangePasswordUiState.ChangePassword(
                input = ChangePasswordInput(
                    oldPassword = "ab",
                    newPassword = "",
                    confirmPassword = "x",
                ),
                validationError = ChangePasswordInputError(
                    oldPassword = "*Too short",
                    newPassword = "*Required",
                    confirmPassword = "*Confirm password should be same as new password",
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun ResetPasswordPanelPreview() {
    AppTheme {
        ChangePasswordScreenContent(
            uiState = ChangePasswordUiState.ResetPassword(
                email = "student@example.com",
                validationError = "",
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
private fun ResetPasswordPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        ChangePasswordScreenContent(
            uiState = ChangePasswordUiState.ResetPassword(
                email = "",
                validationError = "*Required",
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onUiEvent = {},
        )
    }
}
