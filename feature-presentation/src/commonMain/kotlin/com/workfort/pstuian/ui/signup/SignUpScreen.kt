package com.workfort.pstuian.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.ui.signup.composable.Handle
import com.workfort.pstuian.ui.signup.composable.SignUpContentPanel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel,
    facultyId: Int?,
    batchId: Int?,
    onNavigateBack: () -> Unit,
    onNavigateToFacultyPicker: (FacultySelectionMode, Int?, Int?) -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = facultyId, key2 = batchId) {
        if (facultyId != null) {
            if (batchId == null) {
                viewModel.onEvent(SignUpUiEvent.OnChangeFaculty(facultyId))
            } else {
                viewModel.onEvent(SignUpUiEvent.OnChangeBatch(batchId))
            }
        }
    }

    SignUpContentPanel(
        displayState = uiState,
        onUiEvent = { event ->
            when (event) {
                is SignUpUiEvent.OnClickTermsAndConditions ->
                    onOpenUrl("https://pstuian.com/terms-and-conditions")
                is SignUpUiEvent.OnClickPrivacyPolicy ->
                    onOpenUrl("https://pstuian.com/privacy-policy")
                else -> viewModel.onEvent(event)
            }
        },
    )

    uiState.messageState?.Handle {
        viewModel.onEvent(it)
    }

    uiState.navigationState?.let { navigationState ->
        when (navigationState) {
            is NavigationState.GoBack -> {
                onNavigateBack()
            }
            is NavigationState.GoToFacultyPickerScreen -> {
                onNavigateToFacultyPicker(
                    navigationState.mode,
                    navigationState.facultyId,
                    navigationState.batchId
                )
            }
        }
        viewModel.onEvent(SignUpUiEvent.NavigationConsumed)
    }
}
