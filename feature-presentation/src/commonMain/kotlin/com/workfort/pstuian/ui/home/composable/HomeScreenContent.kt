package com.workfort.pstuian.ui.home.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.Slider
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.common.theme.TextStyle
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
                    ProfileView(isSignedInUser = (uiState as? HomeUiState.Content)?.isSignedInUser ?: false, onUiEvent)

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
private fun ProfileView(isSignedInUser: Boolean, onUiEvent: (HomeUiEvent) -> Unit) {
    Box(
        modifier = Modifier.padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isSignedInUser) {
            AppBarIconButton(
                icon = Icons.Default.AccountCircle,
                onClick = { onUiEvent(HomeUiEvent.UserProfileClicked) },
            )
        } else {
            TextButton(
                onClick = { onUiEvent(HomeUiEvent.SignInClicked) },
                modifier = Modifier.height(36.dp),
            ) {
                Text(
                    text = stringResource(Res.string.txt_sign_in),
                    style = TextStyle.label1.copy(color = AppColors.textPrimary),
                )
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
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
fun HomeScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        HomeScreenContent(
            uiState = mockUiState(),
            onUiEvent = {},
        )
    }
}

private fun mockUiState() = HomeUiState.Content(
    isSignedInUser = false,
    sliderState = HomeUiState.SliderState.Available(
        sliders = listOf(
            Slider(id = 1, title = "Slider 1", imageUrl = ""),
            Slider(id = 2, title = "Slider 2", imageUrl = ""),
            Slider(id = 3, title = "Slider 3", imageUrl = ""),
        )
    ),
    facultyState = HomeUiState.FacultyState.Available(
        faculties = listOf(
            Faculty(id = 1, shortTitle = "CSE", title = "Computer Science and Engineering", icon = ""),
            Faculty(id = 2, shortTitle = "Ag", title = "Agriculture", icon = ""),
            Faculty(id = 3, shortTitle = "BA", title = "Business Administration", icon = ""),
        )
    )
)
