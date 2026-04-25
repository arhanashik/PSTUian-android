package com.workfort.pstuian.ui.profile.employeeprofile.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.profile.common.composable.ProfileContentPanel
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.employeeprofile.EmployeeProfileDisplayDataMapper

@Composable
internal fun EmployeeProfileScreenContent(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    AppScaffold {
        ProfileContentPanel(
            uiState = uiState,
            onUiEvent = onUiEvent,
            screenTitle = "Profile",
        ) { expanded, onDismiss, isSignedIn, onEvent ->
            EmployeeProfileOptionsDropdown(expanded, isSignedIn, onDismiss, onEvent)
        }
    }
}

private fun mockEmployeeProfile(withBio: Boolean = true) = EmployeeProfile(
    employee = User.Employee(
        userId = "12",
        name = "Md. Anisur Rahman",
        email = "anisur.rahman@pstu.ac.bd",
        facultyId = 1,
        phone = "+880 1711-222222",
        address = "Patuakhali, Bangladesh",
        bio = if (withBio) "Administrative officer supporting academic operations." else null,
        blood = "B+",
        imageUrl = null,
        id = 12,
        designation = "Office Assistant",
        department = "Administration",
    ),
    faculty = Faculty(id = 1, shortTitle = "CSE", title = "Computer Science & Engineering", icon = null),
    isSignedIn = false,
)

private fun mockUiState(
    isSignedIn: Boolean = false,
    withBio: Boolean = true,
    selectedTab: Int = 0,
): ProfileUiState {
    val mapper = EmployeeProfileDisplayDataMapper()
    val mockProfile = mockEmployeeProfile(withBio).copy(isSignedIn = isSignedIn)
    return ProfileUiState.Content(
        headerDisplayData = mapper.mapHeaderData(mockProfile),
        academicContents = mapper.mapAcademicContents(mockProfile),
        connectContents = mapper.mapConnectContents(mockProfile),
        isSignedIn = isSignedIn,
        selectedTabIndex = selectedTab,
    )
}

@Preview(showBackground = true, name = "Light - Not Signed In")
@Composable
fun EmployeeProfileScreenContentPreview() {
    AppTheme {
        EmployeeProfileScreenContent(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Dark - Not Signed In")
@Composable
fun EmployeeProfileScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        EmployeeProfileScreenContent(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Signed In - Connect Tab")
@Composable
fun EmployeeProfileScreenContentSignedInPreview() {
    AppTheme {
        EmployeeProfileScreenContent(
            uiState = mockUiState(isSignedIn = true, selectedTab = 1),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "No Bio")
@Composable
fun EmployeeProfileScreenContentNoBioPreview() {
    AppTheme {
        EmployeeProfileScreenContent(
            uiState = mockUiState(isSignedIn = true, withBio = false),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun EmployeeProfileScreenContentLoadingPreview() {
    AppTheme {
        EmployeeProfileScreenContent(uiState = ProfileUiState.Loading, onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun EmployeeProfileScreenContentErrorPreview() {
    AppTheme {
        EmployeeProfileScreenContent(
            uiState = ProfileUiState.Error("Failed to load profile"),
            onUiEvent = {},
        )
    }
}