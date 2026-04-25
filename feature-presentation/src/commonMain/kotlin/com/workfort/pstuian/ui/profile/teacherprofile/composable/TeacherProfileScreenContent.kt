package com.workfort.pstuian.ui.profile.teacherprofile.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.profile.common.composable.ProfileContentPanel
import com.workfort.pstuian.ui.profile.employeeprofile.composable.EmployeeProfileOptionsDropdown
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.teacherprofile.TeacherProfileDisplayDataMapper

@Composable
internal fun TeacherProfileScreenContent(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    AppScaffold {
        ProfileContentPanel(
            uiState = uiState,
            onUiEvent = onUiEvent,
            screenTitle = "Profile",
        ) { expanded, onDismiss, isSignedIn, onUiEvent ->
            TeacherProfileOptionsDropdown(expanded, isSignedIn, onDismiss, onUiEvent)
        }
    }
}

private fun mockTeacherProfile(withBio: Boolean = true) = TeacherProfile(
    teacher = User.Teacher(
        userId = "7",
        name = "Dr. Mahmud Rahman",
        email = "mahmud.rahman@pstu.ac.bd",
        facultyId = 1,
        phone = "+880 1711-111111",
        address = "Patuakhali, Bangladesh",
        bio = if (withBio) "Assistant Professor in CSE with interest in distributed systems." else null,
        blood = "A+",
        imageUrl = null,
        id = 7,
        designation = "Assistant Professor",
        linkedIn = "https://linkedin.com/in/mahmud",
        fbLink = "https://facebook.com/mahmud",
        department = "Computer Science & Engineering",
        description = "Focus on backend systems and cloud-native architecture.",
    ),
    faculty = Faculty(id = 1, shortTitle = "CSE", title = "Computer Science & Engineering", icon = null),
    isSignedIn = false,
)

private fun mockUiState(
    isSignedIn: Boolean = false,
    withBio: Boolean = true,
    selectedTab: Int = 0,
): ProfileUiState {
    val mapper = TeacherProfileDisplayDataMapper()
    val mockProfile = mockTeacherProfile(withBio).copy(isSignedIn = isSignedIn)
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
fun TeacherProfileScreenContentPreview() {
    AppTheme {
        TeacherProfileScreenContent(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Dark - Not Signed In")
@Composable
fun TeacherProfileScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        TeacherProfileScreenContent(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Signed In - Connect Tab")
@Composable
fun TeacherProfileScreenContentSignedInPreview() {
    AppTheme {
        TeacherProfileScreenContent(
            uiState = mockUiState(isSignedIn = true, selectedTab = 1),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "No Bio")
@Composable
fun TeacherProfileScreenContentNoBioPreview() {
    AppTheme {
        TeacherProfileScreenContent(
            uiState = mockUiState(isSignedIn = true, withBio = false),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun TeacherProfileScreenContentLoadingPreview() {
    AppTheme {
        TeacherProfileScreenContent(uiState = ProfileUiState.Loading, onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun TeacherProfileScreenContentErrorPreview() {
    AppTheme {
        TeacherProfileScreenContent(
            uiState = ProfileUiState.Error("Failed to load profile"),
            onUiEvent = {},
        )
    }
}