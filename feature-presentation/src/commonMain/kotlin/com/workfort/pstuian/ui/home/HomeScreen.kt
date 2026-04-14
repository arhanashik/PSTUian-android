package com.workfort.pstuian.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.home.composable.HomeContentPanel
import com.workfort.pstuian.ui.home.state.NavigationState

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigateToSignIn: () -> Unit,
    navigateToProfile: (userType: UserType, userId: Int) -> Unit,
    navigateToNotification: () -> Unit,
    navigateToFaculty: (FacultyEntity) -> Unit,
    navigateToImagePreview: (url: String) -> Unit,
    navigateToContactUs: () -> Unit,
    navigateToDonors: () -> Unit,
    navigateToBloodDonationRequest: () -> Unit,
    navigateToCheckIn: () -> Unit,
    navigateToDonate: () -> Unit,
    navigateToSettings: () -> Unit,
    openBrowser: (url: String) -> Unit,
    openStore: () -> Unit,
    requestNotificationPermission: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val messageState by viewModel.messageState.collectAsState()
    val navigationState by viewModel.navigationState.collectAsState()

    LaunchedEffect(key1 = navigationState) {
        when (val state = navigationState) {
            is NavigationState.SplashScreen -> {
                // This is handled by a splash route or similar in KMP
                viewModel.navigationConsumed()
            }
            is NavigationState.SignInScreen -> {
                navigateToSignIn()
                viewModel.navigationConsumed()
            }
            is NavigationState.GoToProfileScreen -> {
                navigateToProfile(state.userType, state.userId)
                viewModel.navigationConsumed()
            }
            is NavigationState.NotificationScreen -> {
                navigateToNotification()
                viewModel.navigationConsumed()
            }
            is NavigationState.FacultyScreen -> {
                navigateToFaculty(state.faculty)
                viewModel.navigationConsumed()
            }
            is NavigationState.ImagePreviewScreen -> {
                navigateToImagePreview(state.url)
                viewModel.navigationConsumed()
            }
            is NavigationState.ContactUsScreen -> {
                navigateToContactUs()
                viewModel.navigationConsumed()
            }
            is NavigationState.DonorsScreen -> {
                navigateToDonors()
                viewModel.navigationConsumed()
            }
            is NavigationState.BloodDonationRequestScreen -> {
                navigateToBloodDonationRequest()
                viewModel.navigationConsumed()
            }
            is NavigationState.CheckInScreen -> {
                navigateToCheckIn()
                viewModel.navigationConsumed()
            }
            is NavigationState.DonateScreen -> {
                navigateToDonate()
                viewModel.navigationConsumed()
            }
            is NavigationState.SettingsScreen -> {
                navigateToSettings()
                viewModel.navigationConsumed()
            }
            null -> Unit
        }
    }

    Scaffold { paddingValues ->
        HomeContentPanel(
            modifier = modifier.padding(paddingValues),
            uiState = uiState,
            messageState = messageState,
            onUiEvent = { event ->
                when (event) {
                    is HomeUiEvent.None -> Unit
                    is HomeUiEvent.LoadInitialData -> viewModel.onUiReady()
                    is HomeUiEvent.GetSliders -> viewModel.getSliders()
                    is HomeUiEvent.GetFaculties -> viewModel.getFaculties()
                    is HomeUiEvent.GetUserProfile -> viewModel.getUserProfile()
                    is HomeUiEvent.OnClickSignIn -> viewModel.onClickSignIn()
                    is HomeUiEvent.OnClickUserProfile -> viewModel.onClickUserProfile()
                    is HomeUiEvent.OnClickNotification -> viewModel.onClickNotification()
                    is HomeUiEvent.OnScrollSlider -> viewModel.onScrollSlider(event.position)
                    is HomeUiEvent.OnClickSlider -> viewModel.onClickSlider(event.slider)
                    is HomeUiEvent.OnClickFaculty -> viewModel.onClickFaculty(event.faculty)
                    is HomeUiEvent.OnClickActionItem -> {
                        when (event.actionItem.action) {
                            Action.AdmissionSupport -> openBrowser(NetworkConst.Remote.PSTU_WEBSITE)
                            Action.Donors -> viewModel.onClickDonors()
                            Action.VarsityWebsite -> openBrowser(NetworkConst.Remote.PSTU_WEBSITE)
                            Action.ContactUs -> viewModel.onClickContactUs()
                            Action.RequestBloodDonation -> viewModel.onClickRequestBloodDonation()
                            Action.CheckIn -> viewModel.onClickCheckIn()
                            Action.RateApp -> openStore()
                            Action.ClearData -> viewModel.onClickClearData()
                            Action.Settings -> viewModel.onClickSettings()
                            Action.Donate -> viewModel.onClickDonate()
                        }
                    }
                    is HomeUiEvent.OnSignIn -> viewModel.onClickSignIn()
                    is HomeUiEvent.OnRequestNotificationPermission -> {
                        viewModel.messageConsumed()
                        requestNotificationPermission()
                    }
                    is HomeUiEvent.OnClearData -> viewModel.clearAllData()
                    is HomeUiEvent.MessageConsumed -> viewModel.messageConsumed()
                    is HomeUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
                }
            },
        )
    }
}
