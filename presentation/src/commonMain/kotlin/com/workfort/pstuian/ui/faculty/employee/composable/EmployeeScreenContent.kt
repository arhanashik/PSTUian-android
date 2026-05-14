package com.workfort.pstuian.ui.faculty.employee.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeUiEvent
import com.workfort.pstuian.ui.faculty.employee.state.EmployeeUiState

@Composable
internal fun EmployeeScreenContent(
    uiState: EmployeeUiState,
    onUiEvent: (EmployeeUiEvent) -> Unit,
) {
    when (uiState) {
        is EmployeeUiState.None -> Unit
        is EmployeeUiState.Content -> EmployeeContentPanel(uiState, onUiEvent)
        is EmployeeUiState.Error -> {
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

private fun mockEmployees() = listOf(
    User.Employee(
        name = "Mizanur Rahman",
        email = "mizanur@pstu.ac.bd",
        facultyId = 1,
        phone = "+8801711000002",
        address = "Patuakhali",
        bio = null,
        blood = "B+",
        imageUrl = null,
        userId = 7,
        designation = "Office Assistant",
        department = "Admin",
    ),
)

@Preview(showBackground = true, name = "Employee - Content")
@Composable
private fun EmployeeScreenContentPreview() {
    AppTheme {
        EmployeeScreenContent(
            uiState = EmployeeUiState.Content(employees = mockEmployees()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Employee - Dark Content")
@Composable
private fun EmployeeScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        EmployeeScreenContent(
            uiState = EmployeeUiState.Content(employees = mockEmployees()),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Employee - Loading")
@Composable
private fun EmployeeScreenContentLoadingPreview() {
    AppTheme {
        EmployeeScreenContent(
            uiState = EmployeeUiState.Content(isLoading = true),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Employee - Empty")
@Composable
private fun EmployeeScreenContentEmptyPreview() {
    AppTheme {
        EmployeeScreenContent(
            uiState = EmployeeUiState.Content(),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Employee - Error")
@Composable
private fun EmployeeScreenContentErrorPreview() {
    AppTheme {
        EmployeeScreenContent(
            uiState = EmployeeUiState.Error("Failed to load employees"),
            onUiEvent = {},
        )
    }
}
