package com.workfort.pstuian.ui.studentprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.studentprofile.composable.StudentProfileContentPanel
import com.workfort.pstuian.ui.studentprofile.state.NavigationState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiEvent

@Composable
fun StudentProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentProfileViewModel,
    navigateBack: () -> Unit,
    navigateToImagePreview: (url: String) -> Unit,
    navigateToImageUpload: (userId: Int, userType: String) -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToDownloadCv: (userId: Int, userType: String, url: String) -> Unit,
    navigateToUploadCv: (userId: Int, userType: String) -> Unit,
    navigateToMyBloodDonationList: (userId: Int, userType: String) -> Unit,
    navigateToMyCheckInList: (userId: Int, userType: String) -> Unit,
    navigateToMyDeviceList: () -> Unit,
    navigateToStudentProfileEdit: (userId: Int, mode: Int) -> Unit,
    navigateToDeleteAccount: () -> Unit,
    callTo: (phoneNumber: String) -> Unit,
    sendEmail: (email: String) -> Unit,
    openBrowser: (url: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.navigationState) {
        when (val navigationState = uiState.navigationState) {
            null -> Unit
            is NavigationState.GoBack -> navigateBack()
            is NavigationState.ImagePreviewScreen -> navigateToImagePreview(navigationState.encodedImageUrl)
            is NavigationState.ImageUploadScreen -> navigateToImageUpload(
                navigationState.userId,
                navigationState.userType.type,
            )
            is NavigationState.ChangePasswordScreen -> navigateToChangePassword()
            is NavigationState.DownloadCvScreen -> navigateToDownloadCv(
                navigationState.userId,
                navigationState.userType.type,
                navigationState.url,
            )
            is NavigationState.UploadCvScreen -> navigateToUploadCv(
                navigationState.userId,
                navigationState.userType.type,
            )
            is NavigationState.MyBloodDonationListScreen -> navigateToMyBloodDonationList(
                navigationState.userId,
                navigationState.userType.type,
            )
            is NavigationState.MyCheckInListScreen -> navigateToMyCheckInList(
                navigationState.userId,
                navigationState.userType.type,
            )
            is NavigationState.MyDeviceListScreen -> navigateToMyDeviceList()
            is NavigationState.StudentProfileEditScreen -> navigateToStudentProfileEdit(
                navigationState.userId,
                navigationState.action.mode,
            )
            is NavigationState.DeleteAccountScreen -> navigateToDeleteAccount()
        }
        if (uiState.navigationState != null) {
            viewModel.navigationConsumed()
        }
    }

    StudentProfileContentPanel(
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is StudentProfileUiEvent.LoadProfile -> viewModel.loadProfile()
                is StudentProfileUiEvent.ClickBack -> viewModel.onClickBack()
                is StudentProfileUiEvent.ClickImage -> viewModel.onClickImage(event.url)
                is StudentProfileUiEvent.ClickCall -> viewModel.onClickCall()
                is StudentProfileUiEvent.ClickEmail -> viewModel.onClickEmail()
                is StudentProfileUiEvent.ClickSignOut -> viewModel.onClickSignOut()
                is StudentProfileUiEvent.ClickTab -> viewModel.onClickTab(event.index)
                is StudentProfileUiEvent.ClickRefresh -> viewModel.onClickRefresh()
                is StudentProfileUiEvent.ClickChangeImage -> viewModel.onClickChangeImage()
                is StudentProfileUiEvent.ClickEditBio -> viewModel.onClickEditBio()
                is StudentProfileUiEvent.ClickEdit -> viewModel.onClickEdit(event.selectedTabIndex)
                is StudentProfileUiEvent.ClickMyBloodDonationList -> viewModel.onClickMyBloodDonationList()
                is StudentProfileUiEvent.ClickChangePassword -> viewModel.onClickChangePassword()
                is StudentProfileUiEvent.ClickDownloadCv -> viewModel.onClickDownloadCv(event.url)
                is StudentProfileUiEvent.ClickUploadCv -> viewModel.onClickUploadCv()
                is StudentProfileUiEvent.ClickMyCheckInList -> viewModel.onClickMyCheckInList()
                is StudentProfileUiEvent.ClickMyDeviceList -> viewModel.onClickMyDeviceList()
                is StudentProfileUiEvent.ClickDeleteAccount -> viewModel.onClickDeleteAccount()
                is StudentProfileUiEvent.ChangeProfileImage -> viewModel.changeProfileImage(event.imageUrl)
                is StudentProfileUiEvent.ChangeBio -> viewModel.changeBio(event.newBio)
                is StudentProfileUiEvent.SignOut -> viewModel.signOut()
                is StudentProfileUiEvent.OnCall -> callTo(event.phoneNumber)
                is StudentProfileUiEvent.OnEmail -> sendEmail(event.email)
                is StudentProfileUiEvent.MessageConsumed -> viewModel.messageConsumed()
                is StudentProfileUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
            }
        }
    )
}
