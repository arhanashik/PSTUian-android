package com.workfort.pstuian.view.ui.teacherprofile

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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.model.ProfileInfoItem
import com.workfort.pstuian.model.ProfileInfoItemAction
import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.reducer.ui.teacherprofile.TeacherProfileScreenState
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.hint_bio
import pstuian.shared.generated.resources.msg_sign_out
import pstuian.shared.generated.resources.txt_academic
import pstuian.shared.generated.resources.txt_account
import pstuian.shared.generated.resources.txt_address
import pstuian.shared.generated.resources.txt_blood_group
import pstuian.shared.generated.resources.txt_call
import pstuian.shared.generated.resources.txt_change_bio
import pstuian.shared.generated.resources.txt_change_password
import pstuian.shared.generated.resources.txt_connect
import pstuian.shared.generated.resources.txt_delete_account
import pstuian.shared.generated.resources.txt_department
import pstuian.shared.generated.resources.txt_designation
import pstuian.shared.generated.resources.txt_devices
import pstuian.shared.generated.resources.txt_edit
import pstuian.shared.generated.resources.txt_email
import pstuian.shared.generated.resources.txt_facebook
import pstuian.shared.generated.resources.txt_faculty
import pstuian.shared.generated.resources.txt_linked_in
import pstuian.shared.generated.resources.txt_msg_call
import pstuian.shared.generated.resources.txt_msg_email
import pstuian.shared.generated.resources.txt_name
import pstuian.shared.generated.resources.txt_option
import pstuian.shared.generated.resources.txt_password
import pstuian.shared.generated.resources.txt_phone
import pstuian.shared.generated.resources.txt_sign_out
import pstuian.shared.generated.resources.txt_signed_in_devices
import pstuian.shared.generated.resources.txt_title_call
import pstuian.shared.generated.resources.txt_title_email
import pstuian.shared.generated.resources.txt_update
import com.workfort.pstuian.view.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.view.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.view.ui.common.component.ProfileInfoListView
import com.workfort.pstuian.view.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.view.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.view.ui.common.component.ShowInputDialog
import com.workfort.pstuian.view.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.view.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.view.ui.common.component.TabView
import com.workfort.pstuian.view.ui.common.component.TitleTextSmall
import com.workfort.pstuian.viewmodel.teacherprofile.TeacherProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun TeacherProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: TeacherProfileViewModel,
    onCall: (String) -> Unit,
    onEmail: (String) -> Unit,
    onBrowser: (String) -> Unit,
    onBack: () -> Unit,
    onImagePreview: (String) -> Unit,
    onChangeImage: (userId: Int, userType: String) -> Unit,
    onChangePassword: () -> Unit,
    onMyDeviceList: () -> Unit,
    onEdit: (userId: Int, mode: Int) -> Unit,
    onDeleteAccount: () -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<TeacherProfileScreenUiEvent>(TeacherProfileScreenUiEvent.None)
    }
    var profileInfoItemAction by remember {
        mutableStateOf<ProfileInfoItemAction>(ProfileInfoItemAction.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = TeacherProfileScreenUiEvent.OnLoadProfile
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
    val navState = navigationState
    if (navState != null) {
        navState.Handle(
            onBack = onBack,
            onChangeImage = onChangeImage,
            onChangePassword = onChangePassword,
            onMyDeviceList = onMyDeviceList,
            onEdit = onEdit,
            onDeleteAccount = onDeleteAccount,
            onImagePreview = onImagePreview,
        ) {
            uiEvent = it
        }
    }
}

    with(uiEvent) {
        when (this) {
            is TeacherProfileScreenUiEvent.None -> Unit
            is TeacherProfileScreenUiEvent.OnLoadProfile -> viewModel.loadProfile()
            is TeacherProfileScreenUiEvent.OnSignOut -> viewModel.signOut()
            is TeacherProfileScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is TeacherProfileScreenUiEvent.OnClickCall -> viewModel.onClickCall()
            is TeacherProfileScreenUiEvent.OnClickSignOut -> viewModel.onClickSignOut()
            is TeacherProfileScreenUiEvent.OnClickTab -> viewModel.onClickTab(index)
            is TeacherProfileScreenUiEvent.OnClickRefresh -> viewModel.onClickRefresh()
            is TeacherProfileScreenUiEvent.OnClickChangeImage -> viewModel.onClickChangeImage()
            is TeacherProfileScreenUiEvent.OnClickEditBio -> viewModel.onClickEditBio()
            is TeacherProfileScreenUiEvent.OnClickAction -> profileInfoItemAction = actionItem
            is TeacherProfileScreenUiEvent.OnClickEdit -> viewModel.onClickEdit(selectedTabIndex)
            is TeacherProfileScreenUiEvent.OnClickImage -> viewModel.onClickImage(url)
            is TeacherProfileScreenUiEvent.OnEditBio -> viewModel.changeBio(newBio)
            is TeacherProfileScreenUiEvent.OnCall -> onCall(phoneNumber)
            is TeacherProfileScreenUiEvent.OnEmail -> onEmail(email)
            is TeacherProfileScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is TeacherProfileScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = TeacherProfileScreenUiEvent.None
    }

    with(profileInfoItemAction) {
        when (this) {
            is ProfileInfoItemAction.None -> Unit
            is ProfileInfoItemAction.Edit -> Unit
            is ProfileInfoItemAction.Call -> viewModel.onClickCall()
            is ProfileInfoItemAction.Email -> viewModel.onClickEmail()
            is ProfileInfoItemAction.DownloadCv -> Unit
            is ProfileInfoItemAction.Link -> onBrowser(url)
            is ProfileInfoItemAction.Password -> viewModel.onClickChangePassword()
            is ProfileInfoItemAction.UploadCv -> Unit
            is ProfileInfoItemAction.BloodDonationList -> Unit
            is ProfileInfoItemAction.CheckInList -> Unit
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
    displayState: TeacherProfileScreenState.DisplayState,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
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
                                TeacherProfileScreenUiEvent.OnClickEdit(
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
    profile: TeacherProfile,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabs = getTeacherTabs(profile.isSignedIn)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    LaunchedEffect(key1 = selectedTabIndex) {
        onUiEvent(TeacherProfileScreenUiEvent.OnClickTab(selectedTabIndex))
    }

    Column(
        modifier = modifier.fillMaxSize(),
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
                onClick = { onUiEvent(TeacherProfileScreenUiEvent.OnClickBack) },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.teacher.imageUrl
                LoadAsyncUserImage(
                    modifier = if (imageUrl.isNullOrEmpty()) {
                        Modifier
                    } else {
                        Modifier.clickable {
                            onUiEvent(TeacherProfileScreenUiEvent.OnClickImage(imageUrl))
                        }
                    },
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
                                onUiEvent(TeacherProfileScreenUiEvent.OnClickChangeImage)
                            },
                    )
                }
            }
            IconButton(
                onClick = {
                    onUiEvent(
                        if (profile.isSignedIn) {
                            TeacherProfileScreenUiEvent.OnClickSignOut
                        } else {
                            TeacherProfileScreenUiEvent.OnClickCall
                        }
                    )
                },
            ) {
                if (profile.isSignedIn) {
                    Icon(
                        Icons.Default.Logout,
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
            text = profile.teacher.name,
        )
        profile.teacher.bio?.let { bio ->
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
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
                        onUiEvent(TeacherProfileScreenUiEvent.OnClickEditBio)
                    }
                    .padding(horizontal = 8.dp),
            )
        }
        TabView(
            modifier = Modifier.padding(top = 16.dp),
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getTeacherAcademicTabItems(profile).ProfileInfoListView {
                    onUiEvent(TeacherProfileScreenUiEvent.OnClickAction(it.action))
                }
                1 -> getTeacherConnectTabItems(profile).ProfileInfoListView {
                    onUiEvent(TeacherProfileScreenUiEvent.OnClickAction(it.action))
                }
                2 -> getTeacherOptionTabItems().ProfileInfoListView {
                    onUiEvent(TeacherProfileScreenUiEvent.OnClickAction(it.action))
                }
            }
        }
    }
}

@Composable
private fun TeacherProfileScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
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
private fun TeacherProfileScreenState.NavigationState.Handle(
    onBack: () -> Unit,
    onChangeImage: (userId: Int, userType: String) -> Unit,
    onChangePassword: () -> Unit,
    onMyDeviceList: () -> Unit,
    onEdit: (userId: Int, mode: Int) -> Unit,
    onDeleteAccount: () -> Unit,
    onImagePreview: (String) -> Unit,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
) {
    LaunchedEffect(this) {
        when (this@Handle) {
            is TeacherProfileScreenState.NavigationState.GoBack -> onBack()
            is TeacherProfileScreenState.NavigationState.ImageUploadScreen -> {
                onChangeImage(userId, userType.type)
            }
            is TeacherProfileScreenState.NavigationState.ChangePasswordScreen -> {
                onChangePassword()
            }
            is TeacherProfileScreenState.NavigationState.MyDeviceListScreen -> {
                onMyDeviceList()
            }
            is TeacherProfileScreenState.NavigationState.TeacherProfileEditScreen -> {
                onEdit(userId, action.mode)
            }
            is TeacherProfileScreenState.NavigationState.DeleteAccountScreen -> {
                onDeleteAccount()
            }
            is TeacherProfileScreenState.NavigationState.ImagePreviewScreen -> {
                onImagePreview(encodedImageUrl)
            }
        }
        onUiEvent(TeacherProfileScreenUiEvent.NavigationConsumed)
    }
}

@Composable
private fun TeacherProfileScreenState.DisplayState.ProfileState.Handle(
    modifier: Modifier,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is TeacherProfileScreenState.DisplayState.ProfileState.None -> Unit
        is TeacherProfileScreenState.DisplayState.ProfileState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
        is TeacherProfileScreenState.DisplayState.ProfileState.Available -> {
            ProfileView(modifier, profile, onUiEvent)
        }
        is TeacherProfileScreenState.DisplayState.ProfileState.Error -> {
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
private fun TeacherProfileScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is TeacherProfileScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is TeacherProfileScreenState.DisplayState.MessageState.InputBio -> {
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
                    onUiEvent(TeacherProfileScreenUiEvent.OnEditBio(it))
                },
                onDismiss = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
        is TeacherProfileScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(TeacherProfileScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
        is TeacherProfileScreenState.DisplayState.MessageState.Email -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Email,
                title = stringResource(Res.string.txt_title_email),
                message = stringResource(Res.string.txt_msg_email).plus(" $email"),
                confirmButtonText = stringResource(Res.string.txt_email),
                onConfirm = {
                    onUiEvent(TeacherProfileScreenUiEvent.OnEmail(email))
                },
                onDismiss = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
        is TeacherProfileScreenState.DisplayState.MessageState.ConfirmSignOut -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_out),
                message = stringResource(Res.string.msg_sign_out),
                onConfirm = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                    onUiEvent(TeacherProfileScreenUiEvent.OnSignOut)
                },
                onDismiss = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                },
            )
        }
        is TeacherProfileScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                onConfirm = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                },
            )
        }
        is TeacherProfileScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(TeacherProfileScreenUiEvent.MessageConsumed)
                },
            )
        }
    }
}

@Composable
fun getTeacherTabs(isSignedIn: Boolean): List<String> {
    val tabs = mutableListOf(
        stringResource(Res.string.txt_academic),
        stringResource(Res.string.txt_connect),
    )
    if (isSignedIn) {
        tabs.add(stringResource(Res.string.txt_option))
    }
    return tabs
}

@Composable
fun getTeacherAcademicTabItems(profile: TeacherProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.teacher.name),
    ProfileInfoItem(stringResource(Res.string.txt_designation), profile.teacher.designation),
    ProfileInfoItem(stringResource(Res.string.txt_department), profile.teacher.department),
    ProfileInfoItem(stringResource(Res.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(stringResource(Res.string.txt_blood_group), profile.teacher.blood ?: "~"),
)

@Composable
fun getTeacherConnectTabItems(profile: TeacherProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_address), profile.teacher.address ?: "~"),
    ProfileInfoItem(
        stringResource(Res.string.txt_phone),
        profile.teacher.phone ?: "~",
        if (profile.teacher.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.teacher.phone.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_email),
        profile.teacher.email ?: "~",
        if (profile.teacher.email.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Email(profile.teacher.email.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_linked_in),
        profile.teacher.linkedIn ?: "~",
        if (profile.teacher.linkedIn.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.teacher.linkedIn.orEmpty())
        }
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_facebook),
        profile.teacher.fbLink ?: "~",
        if (profile.teacher.fbLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.teacher.fbLink.orEmpty())
        },
    ),
)

@Composable
fun getTeacherOptionTabItems() = listOf(
    ProfileInfoItem(
        stringResource(Res.string.txt_password),
        stringResource(Res.string.txt_change_password),
        ProfileInfoItemAction.Password,
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