package com.workfort.pstuian.ui.home.composable

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.home.state.HomeUiEvent
import com.workfort.pstuian.ui.home.state.HomeUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name
import pstuian.feature_presentation.generated.resources.txt_sign_in

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    AppScaffold (
        topBar = {
            AppBar(
                title = stringResource(Res.string.app_name),
                actions = {
                    // load signed in user
                    (uiState as? HomeUiState.Content)?.let {
                        ProfileView(uiState.profileState, onUiEvent)
                    }

                    AppBarIconButton(
                        icon = Icons.Default.NotificationsActive,
                        onClick = { onUiEvent(HomeUiEvent.NotificationClicked) },
                    )
                },
            )
        }
    ) {
        when (uiState) {
            is HomeUiState.None -> Unit
            is HomeUiState.Content -> HomeContentPanel(uiState, onUiEvent)
        }
    }
}

@Composable
private fun ProfileView(state: HomeUiState.ProfileState, onUiEvent: (HomeUiEvent) -> Unit) {
    when (state) {
        is HomeUiState.ProfileState.None -> Unit
        is HomeUiState.ProfileState.Loading -> Unit
        is HomeUiState.ProfileState.Available -> {
            LoadAsyncUserImage(
                modifier = Modifier.clickable {
                    onUiEvent(HomeUiEvent.UserProfileClicked)
                },
                url = state.user.imageUrl,
                size = 24.dp,
            )
        }
        is HomeUiState.ProfileState.Error -> {
            TextButton(onClick = { onUiEvent(HomeUiEvent.SignInClicked) }) {
                Text(text = stringResource(Res.string.txt_sign_in))
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenContentPreview() {
    AppTheme {
        HomeScreenContent(
            uiState = mockUiState(),
            onUiEvent = {}
        )
    }
}

@Preview
@Composable
fun HomeScreenContentDarkPreview() {
    AppTheme(darkTheme = true) {
        HomeScreenContent(
            uiState = mockUiState(),
            onUiEvent = {}
        )
    }
}

private fun mockUiState() = HomeUiState.Content(
    profileState = HomeUiState.ProfileState.Available(
        user = User.Student(
            userId = "userId",
            name = "Arhan",
            email = "arhan@example.com",
            facultyId = 1,
            phone = "0123456789",
            address = "PSTU",
            bio = "Student",
            blood = "B+",
            imageUrl = "",
            studentId = "123",
            reg = "456",
            linkedIn = null,
            fbLink = null,
            batchId = 1,
            session = "2020-21",
            cvLink = null,
        )
    ),
    sliderState = HomeUiState.SliderState.Available(
        sliders = listOf(
            SliderEntity(id = 1, title = "Slider 1", imageUrl = ""),
            SliderEntity(id = 2, title = "Slider 2", imageUrl = ""),
            SliderEntity(id = 3, title = "Slider 3", imageUrl = ""),
        )
    ),
    facultyState = HomeUiState.FacultyState.Available(
        faculties = listOf(
            FacultyEntity(id = 1, shortTitle = "CSE", title = "Computer Science and Engineering", icon = ""),
            FacultyEntity(id = 2, shortTitle = "Ag", title = "Agriculture", icon = ""),
            FacultyEntity(id = 3, shortTitle = "BA", title = "Business Administration", icon = ""),
        )
    )
)
