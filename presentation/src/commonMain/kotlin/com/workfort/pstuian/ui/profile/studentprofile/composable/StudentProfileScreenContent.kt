package com.workfort.pstuian.ui.profile.studentprofile.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.studentprofile.StudentProfileDisplayDataMapper

@Composable
internal fun StudentProfileScreenContent(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
    snackbarHost: @Composable () -> Unit = {},
) {
    AppScaffold(snackbarHost = snackbarHost) {
        StudentProfileContentPanel(uiState, onUiEvent)
    }
}

private fun mockProfile(withBio: Boolean = true) = UserProfile.StudentProfile(
    user = User.Student(
        userId = 42,
        name = "Diana Richards",
        email = "diana.richards@pstu.ac.bd",
        facultyId = 1,
        phone = "+880 1711-000000",
        address = "Patuakhali, Bangladesh",
        bio = if (withBio) "Passionate about technology and innovation. CSE graduate." else null,
        blood = "B+",
        imageUrl = null,
        reg = "2018-215-001",
        batchId = 10,
        session = "2018-19",
        linkedIn = "https://linkedin.com/in/diana",
        fbLink = "https://facebook.com/diana",
        cvLink = null,
    ),
    faculty = Faculty(id = 1, shortTitle = "CSE", title = "Computer Science & Engineering", icon = null),
    batch = Batch(id = 10, name = "10th Batch", title = "Batch 10", session = "2018-19", facultyId = 1, totalStudent = 120, registeredStudent = 98),
    isSignedIn = false,
)

private fun mockUiState(
    isSignedIn: Boolean = false,
    withBio: Boolean = true,
    selectedTab: Int = 0,
): ProfileUiState {
    val mapper = StudentProfileDisplayDataMapper()
    val mockProfile = mockProfile(withBio)
    return ProfileUiState.Content(
        headerDisplayData = mapper.mapHeaderData(mockProfile),
        academicContents = mapper.mapAcademicContents(mockProfile),
        connectContents = mapper.mapConnectContents(mockProfile),
        isSignedIn = isSignedIn,
        userPresenceDisplayData = UserPresenceDisplayData(),
        selectedTabIndex = selectedTab,
    )
}

@Preview(showBackground = true, name = "Light – Not Signed In")
@Composable
fun StudentProfileContentPanelPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Dark – Not Signed In")
@Composable
fun StudentProfileContentPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        StudentProfileContentPanel(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Signed In – Academic Tab")
@Composable
fun StudentProfileContentPanelSignedInPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(isSignedIn = true), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Signed In – Connect Tab")
@Composable
fun StudentProfileContentPanelConnectTabPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(isSignedIn = true, selectedTab = 1), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "No Bio")
@Composable
fun StudentProfileContentPanelNoBioPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(isSignedIn = true, withBio = false), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Shimmer Loading")
@Composable
fun StudentProfileContentPanelLoadingPreview() {
    AppTheme {
        StudentProfileContentPanel(
            uiState = ProfileUiState.Loading,
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun StudentProfileContentPanelErrorPreview() {
    AppTheme {
        StudentProfileContentPanel(
            uiState = ProfileUiState.Error("Failed to load profile"),
            onUiEvent = {},
        )
    }
}