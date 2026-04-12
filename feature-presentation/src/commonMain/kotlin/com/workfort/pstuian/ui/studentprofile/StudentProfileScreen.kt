package com.workfort.pstuian.app.ui.common.ui.studentprofile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.ProfileInfoItem
import com.workfort.pstuian.model.ProfileInfoItemAction
import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.reducer.ui.studentprofile.StudentProfileScreenState
import com.workfort.pstuian.common.component.AnimatedErrorView
import com.workfort.pstuian.common.component.LoadAsyncUserImage
import com.workfort.pstuian.common.component.ProfileInfoListView
import com.workfort.pstuian.common.component.ShowConfirmationDialog
import com.workfort.pstuian.common.component.ShowErrorDialog
import com.workfort.pstuian.common.component.ShowInputDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.common.component.ShowSuccessDialog
import com.workfort.pstuian.common.component.TabView
import com.workfort.pstuian.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.commonmodel.studentprofile.StudentProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_option
import pstuian.feature_presentation.generated.resources.txt_name
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_registration_number
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_faculty
import pstuian.feature_presentation.generated.resources.txt_batch
import pstuian.feature_presentation.generated.resources.txt_session
import pstuian.feature_presentation.generated.resources.txt_address
import pstuian.feature_presentation.generated.resources.txt_phone
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_cv
import pstuian.feature_presentation.generated.resources.txt_linked_in
import pstuian.feature_presentation.generated.resources.txt_facebook
import pstuian.feature_presentation.generated.resources.txt_password
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.hint_upload_new_cv
import pstuian.feature_presentation.generated.resources.txt_blood_donation
import pstuian.feature_presentation.generated.resources.txt_my_donation_list
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_signed_in_devices
import pstuian.feature_presentation.generated.resources.txt_account
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_change_bio
import pstuian.feature_presentation.generated.resources.hint_bio
import pstuian.feature_presentation.generated.resources.txt_update
import pstuian.feature_presentation.generated.resources.txt_title_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_title_email
import pstuian.feature_presentation.generated.resources.txt_msg_email
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.msg_sign_out

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
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<StudentProfileScreenUiEvent>(StudentProfileScreenUiEvent.None)
    }
    var profileInfoItemAction by remember {
        mutableStateOf<ProfileInfoItemAction>(ProfileInfoItemAction.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = StudentProfileScreenUiEvent.OnLoadProfile
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(
            navigateBack = navigateBack,
            navigateToImagePreview = navigateToImagePreview,
            navigateToImageUpload = navigateToImageUpload,
            navigateToChangePassword = navigateToChangePassword,
            navigateToDownloadCv = navigateToDownloadCv,
            navigateToUploadCv = navigateToUploadCv,
            navigateToMyBloodDonationList = navigateToMyBloodDonationList,
            navigateToMyCheckInList = navigateToMyCheckInList,
            navigateToMyDeviceList = navigateToMyDeviceList,
            navigateToStudentProfileEdit = navigateToStudentProfileEdit,
            navigateToDeleteAccount = navigateToDeleteAccount,
        ) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is StudentProfileScreenUiEvent.None -> Unit
            is StudentProfileScreenUiEvent.OnLoadProfile -> viewModel.loadProfile()
            is StudentProfileScreenUiEvent.OnSignOut -> viewModel.signOut()
            is StudentProfileScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is StudentProfileScreenUiEvent.OnClickCall -> viewModel.onClickCall()
            is StudentProfileScreenUiEvent.OnClickSignOut -> viewModel.onClickSignOut()
            is StudentProfileScreenUiEvent.OnClickTab -> viewModel.onClickTab(index)
            is StudentProfileScreenUiEvent.OnClickRefresh -> viewModel.onClickRefresh()
            is StudentProfileScreenUiEvent.OnClickChangeImage -> viewModel.onClickChangeImage()
            is StudentProfileScreenUiEvent.OnClickEditBio -> viewModel.onClickEditBio()
            is StudentProfileScreenUiEvent.OnClickAction -> profileInfoItemAction = action
            is StudentProfileScreenUiEvent.OnClickEdit -> viewModel.onClickEdit(selectedTabIndex)
            is StudentProfileScreenUiEvent.OnClickImage -> viewModel.onClickImage(url)
            is StudentProfileScreenUiEvent.OnEditBio -> viewModel.changeBio(newBio)
            is StudentProfileScreenUiEvent.OnCall -> callTo(phoneNumber)
            is StudentProfileScreenUiEvent.OnEmail -> sendEmail(email)
            is StudentProfileScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is StudentProfileScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = StudentProfileScreenUiEvent.None
    }

    with(profileInfoItemAction) {
        when (this) {
            is ProfileInfoItemAction.None -> Unit
            is ProfileInfoItemAction.Edit -> Unit
            is ProfileInfoItemAction.Call -> viewModel.onClickCall()
            is ProfileInfoItemAction.Email -> viewModel.onClickEmail()
            is ProfileInfoItemAction.DownloadCv -> viewModel.onClickDownloadCv(url)
            is ProfileInfoItemAction.Link -> openBrowser(url)
            is ProfileInfoItemAction.Password -> viewModel.onClickChangePassword()
            is ProfileInfoItemAction.UploadCv -> viewModel.onClickUploadCv()
            is ProfileInfoItemAction.BloodDonationList -> viewModel.onClickMyBloodDonationList()
            is ProfileInfoItemAction.CheckInList -> viewModel.onClickMyCheckInList()
            is ProfileInfoItemAction.SignedInDevices -> viewModel.onClickMyDeviceList()
            is ProfileInfoItemAction.DeleteAccount -> viewModel.onClickDeleteAccount()
        }
        profileInfoItemAction = ProfileInfoItemAction.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: StudentProfileScreenState.DisplayState,
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            if (displayState.isSignedIn && displayState.selectedTabIndex < 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    ExtendedFloatingActionButton(
                        expanded = fabButtonExpanded,
                        text = {
                            Text(text = stringResource(Res.string.txt_edit))
                        },
                        onClick = {
                            onUiEvent(
                                StudentProfileScreenUiEvent.OnClickEdit(
                                    displayState.selectedTabIndex,
                                )
                            )
                        },
                        icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        shape = CircleShape,
                    )
                }
            }
        },
    ) { innerPadding ->
        displayState.profileState.Handle(modifier.padding(innerPadding), onUiEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfileView(
    modifier: Modifier,
    profile: StudentProfile,
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabs = getStudentsTabs(profile.isSignedIn)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    LaunchedEffect(key1 = selectedTabIndex) {
        onUiEvent(StudentProfileScreenUiEvent.OnClickTab(selectedTabIndex))
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { onUiEvent(StudentProfileScreenUiEvent.OnClickBack) },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.student.imageUrl
                val imageModifier = if (imageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(StudentProfileScreenUiEvent.OnClickImage(imageUrl))
                    }
                }
                LoadAsyncUserImage(
                    modifier = imageModifier,
                    url = imageUrl,
                    size = 96.dp,
                )
                if (profile.isSignedIn) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .clickable {
                                onUiEvent(StudentProfileScreenUiEvent.OnClickChangeImage)
                            },
                    )
                }
            }
            IconButton(
                onClick = {
                    onUiEvent(
                        if (profile.isSignedIn) {
                            StudentProfileScreenUiEvent.OnClickSignOut
                        } else {
                            StudentProfileScreenUiEvent.OnClickCall
                        }
                    )
                },
            ) {
                if (profile.isSignedIn) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Action button",
                    )
                } else {
                    Icon(
                        Icons.Filled.Call,
                        contentDescription = "Action button",
                    )
                }
            }
        }
        TitleTextSmall(
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            text = profile.student.name,
        )
        profile.student.bio?.let { bio ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = bio,
                textAlign = TextAlign.Center,
            )
        }
        if (profile.isSignedIn) {
            Text(
                text = "Edit Bio",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onUiEvent(StudentProfileScreenUiEvent.OnClickEditBio)
                    }
                    .padding(horizontal = 8.dp),
            )
        }
        TabView(
            tabs = getStudentsTabs(profile.isSignedIn),
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getStudentAcademicTabItems(profile).ProfileInfoListView {
                    onUiEvent(StudentProfileScreenUiEvent.OnClickAction(it.action))
                }
                1 -> getStudentConnectTabItems(profile).ProfileInfoListView {
                    onUiEvent(StudentProfileScreenUiEvent.OnClickAction(it.action))
                }
                2 -> if (profile.isSignedIn) {
                    getStudentOptionTabItems().ProfileInfoListView {
                        onUiEvent(StudentProfileScreenUiEvent.OnClickAction(it.action))
                    }
                }
            }
        }
    }
}

@Composable
private fun getStudentsTabs(isSignedIn: Boolean) = arrayListOf(
    stringResource(Res.string.txt_academic),
    stringResource(Res.string.txt_connect),
).also {
    if (isSignedIn) {
        it.add(stringResource(Res.string.txt_option))
    }
}

@Composable
private fun getStudentAcademicTabItems(profile: StudentProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.student.name),
    ProfileInfoItem(stringResource(Res.string.txt_id), profile.student.id.toString()),
    ProfileInfoItem(stringResource(Res.string.txt_registration_number), profile.student.reg),
    ProfileInfoItem(stringResource(Res.string.txt_blood_group), profile.student.blood ?: "~"),
    ProfileInfoItem(stringResource(Res.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(stringResource(Res.string.txt_batch), profile.batch.name),
    ProfileInfoItem(stringResource(Res.string.txt_session), profile.student.session),
)

@Composable
private fun getStudentConnectTabItems(profile: StudentProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_address), profile.student.address ?: "~"),
    ProfileInfoItem(
        stringResource(Res.string.txt_phone),
        profile.student.phone ?: "~",
        if (profile.student.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.student.phone.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_email),
        profile.student.email ?: "~",
        if (profile.student.email.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Email(profile.student.email.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_cv),
        profile.student.cvLink ?: "~",
        if (profile.student.cvLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.DownloadCv(profile.student.cvLink.orEmpty())
        }
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_linked_in),
        profile.student.linkedIn ?: "~",
        if (profile.student.linkedIn.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.student.linkedIn.orEmpty())
        }
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_facebook),
        profile.student.fbLink ?: "~",
        if (profile.student.fbLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.student.fbLink.orEmpty())
        },
    ),
)

@Composable
private fun getStudentOptionTabItems() = listOf(
    ProfileInfoItem(
        stringResource(Res.string.txt_password),
        stringResource(Res.string.txt_change_password),
        ProfileInfoItemAction.Password,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_cv),
        stringResource(Res.string.hint_upload_new_cv),
        ProfileInfoItemAction.UploadCv,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_blood_donation),
        stringResource(Res.string.txt_my_donation_list),
        ProfileInfoItemAction.BloodDonationList,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_check_in),
        stringResource(Res.string.txt_my_check_in_list),
        ProfileInfoItemAction.CheckInList,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_devices),
        stringResource(Res.string.txt_signed_in_devices),
        ProfileInfoItemAction.SignedInDevices,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_account),
        stringResource(Res.string.txt_delete_account),
        ProfileInfoItemAction.DeleteAccount,
    ),
)

@Composable
private fun StudentProfileScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    val state = messageState
    if (state != null) {
        state.Handle(onUiEvent)
    }
}

@Composable
private fun StudentProfileScreenState.DisplayState.ProfileState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileScreenState.DisplayState.ProfileState.None -> Unit
        is StudentProfileScreenState.DisplayState.ProfileState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
        is StudentProfileScreenState.DisplayState.ProfileState.Available -> {
            ProfileView(modifier, profile, onUiEvent)
        }
        is StudentProfileScreenState.DisplayState.ProfileState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun StudentProfileScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is StudentProfileScreenState.DisplayState.MessageState.InputBio -> {
            ShowInputDialog(
                title = stringResource(Res.string.txt_change_bio),
                label = stringResource(Res.string.hint_bio),
                input = currentBio,
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                maxLength = 150,
                confirmButtonText = stringResource(Res.string.txt_update),
                onConfirm = {
                    onUiEvent(StudentProfileScreenUiEvent.OnEditBio(it))
                },
                onDismiss = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(StudentProfileScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileScreenState.DisplayState.MessageState.Email -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Email,
                title = stringResource(Res.string.txt_title_email),
                message = stringResource(Res.string.txt_msg_email).plus(" $email"),
                confirmButtonText = stringResource(Res.string.txt_email),
                onConfirm = {
                    onUiEvent(StudentProfileScreenUiEvent.OnEmail(email))
                },
                onDismiss = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileScreenState.DisplayState.MessageState.ConfirmSignOut -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_out),
                message = stringResource(Res.string.msg_sign_out),
                onConfirm = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                    onUiEvent(StudentProfileScreenUiEvent.OnSignOut)
                },
                onDismiss = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                },
            )
        }
        is StudentProfileScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                onConfirm = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                },
            )
        }
        is StudentProfileScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(StudentProfileScreenUiEvent.MessageConsumed)
                },
            )
        }
    }
}

@Composable
private fun StudentProfileScreenState.NavigationState.Handle(
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
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileScreenState.NavigationState.GoBack -> {
            navigateBack()
        }
        is StudentProfileScreenState.NavigationState.ImageUploadScreen -> {
            navigateToImageUpload(userId, userType.type)
        }
        is StudentProfileScreenState.NavigationState.ChangePasswordScreen -> {
            navigateToChangePassword()
        }
        is StudentProfileScreenState.NavigationState.DownloadCvScreen -> {
            navigateToDownloadCv(userId, userType.type, url)
        }
        is StudentProfileScreenState.NavigationState.UploadCvScreen -> {
            navigateToUploadCv(userId, userType.type)
        }
        is StudentProfileScreenState.NavigationState.MyBloodDonationListScreen -> {
            navigateToMyBloodDonationList(userId, userType.type)
        }
        is StudentProfileScreenState.NavigationState.MyCheckInListScreen -> {
            navigateToMyCheckInList(userId, userType.type)
        }
        is StudentProfileScreenState.NavigationState.MyDeviceListScreen -> {
            navigateToMyDeviceList()
        }
        is StudentProfileScreenState.NavigationState.StudentProfileEditScreen -> {
            navigateToStudentProfileEdit(userId, action.mode)
        }
        is StudentProfileScreenState.NavigationState.DeleteAccountScreen -> {
            navigateToDeleteAccount()
        }
        is StudentProfileScreenState.NavigationState.ImagePreviewScreen -> {
            navigateToImagePreview(encodedImageUrl)
        }
    }
    onUiEvent(StudentProfileScreenUiEvent.NavigationConsumed)
}