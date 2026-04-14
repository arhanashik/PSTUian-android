package com.workfort.pstuian.ui.teacherprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.teacherprofile.composable.TeacherProfileContentPanel
import com.workfort.pstuian.ui.teacherprofile.state.NavigationState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiEvent

@Composable
fun TeacherProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: TeacherProfileViewModel,
    navigateBack: () -> Unit,
    navigateToImagePreview: (url: String) -> Unit,
    navigateToImageUpload: (userId: Int, userType: String) -> Unit,
    navigateToChangePassword: () -> Unit,
    navigateToMyDeviceList: () -> Unit,
    navigateToTeacherProfileEdit: (userId: Int, mode: Int) -> Unit,
    navigateToDeleteAccount: () -> Unit,
    callTo: (phoneNumber: String) -> Unit,
    sendEmail: (email: String) -> Unit,
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
            is NavigationState.MyDeviceListScreen -> navigateToMyDeviceList()
            is NavigationState.TeacherProfileEditScreen -> navigateToTeacherProfileEdit(
                navigationState.userId,
                navigationState.action.mode,
            )
            is NavigationState.DeleteAccountScreen -> navigateToDeleteAccount()
        }
        if (uiState.navigationState != null) {
            viewModel.navigationConsumed()
        }
    }

    TeacherProfileContentPanel(
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is TeacherProfileUiEvent.LoadProfile -> viewModel.loadProfile()
                is TeacherProfileUiEvent.ClickBack -> viewModel.onClickBack()
                is TeacherProfileUiEvent.ClickImage -> viewModel.onClickImage(event.url)
                is TeacherProfileUiEvent.ClickCall -> viewModel.onClickCall()
                is TeacherProfileUiEvent.ClickEmail -> viewModel.onClickEmail()
                is TeacherProfileUiEvent.ClickSignOut -> viewModel.onClickSignOut()
                is TeacherProfileUiEvent.ClickTab -> viewModel.onClickTab(event.index)
                is TeacherProfileUiEvent.ClickRefresh -> viewModel.onClickRefresh()
                is TeacherProfileUiEvent.ClickChangeImage -> viewModel.onClickChangeImage()
                is TeacherProfileUiEvent.ClickEditBio -> viewModel.onClickEditBio()
                is TeacherProfileUiEvent.ClickEdit -> viewModel.onClickEdit(event.selectedTabIndex)
                is TeacherProfileUiEvent.ClickChangePassword -> viewModel.onClickChangePassword()
                is TeacherProfileUiEvent.ClickMyDeviceList -> viewModel.onClickMyDeviceList()
                is TeacherProfileUiEvent.ClickDeleteAccount -> viewModel.onClickDeleteAccount()
                is TeacherProfileUiEvent.ChangeProfileImage -> viewModel.changeProfileImage(event.imageUrl)
                is TeacherProfileUiEvent.ChangeBio -> viewModel.changeBio(event.newBio)
                is TeacherProfileUiEvent.SignOut -> viewModel.signOut()
                is TeacherProfileUiEvent.OnCall -> callTo(event.phoneNumber)
                is TeacherProfileUiEvent.OnEmail -> sendEmail(event.email)
                is TeacherProfileUiEvent.MessageConsumed -> viewModel.messageConsumed()
                is TeacherProfileUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
            }
        }
    )
}
