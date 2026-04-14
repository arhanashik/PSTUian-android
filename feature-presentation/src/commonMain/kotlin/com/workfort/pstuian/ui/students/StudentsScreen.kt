package com.workfort.pstuian.ui.students

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.students.composable.StudentsContentPanel
import com.workfort.pstuian.ui.students.state.NavigationState
import com.workfort.pstuian.ui.students.state.StudentsUiEvent

@Composable
fun StudentsScreen(
    viewModel: StudentsViewModel,
    navigateBack: () -> Unit,
    navigateToStudentProfile: (studentId: Int) -> Unit,
    callTo: (phoneNumber: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.navigationState) {
        when (val state = uiState.navigationState) {
            is NavigationState.GoBack -> {
                navigateBack()
                viewModel.navigationConsumed()
            }
            is NavigationState.GoToStudentProfile -> {
                navigateToStudentProfile(state.student.id)
                viewModel.navigationConsumed()
            }
            null -> Unit
        }
    }

    StudentsContentPanel(
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is StudentsUiEvent.LoadStudentList -> viewModel.loadStudentList()
                is StudentsUiEvent.ClickBack -> viewModel.onClickBack()
                is StudentsUiEvent.ClickStudent -> viewModel.onClickStudent(event.student)
                is StudentsUiEvent.ClickCall -> viewModel.onClickCall(event.phoneNumber)
                is StudentsUiEvent.Call -> callTo(event.phoneNumber)
                is StudentsUiEvent.MessageConsumed -> viewModel.messageConsumed()
                is StudentsUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
            }
        }
    )
}
