package com.workfort.pstuian.app.ui.studentprofile

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.NavParam
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.app.ui.common.component.ProfileInfoListView
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowInputDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.app.ui.common.component.TabView
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.ProfileInfoItemAction
import com.workfort.pstuian.model.StudentProfile
import com.workfort.pstuian.util.helper.LinkUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun StudentProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentProfileViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<StudentProfileScreenUiEvent>(StudentProfileScreenUiEvent.None)
    }
    var profileInfoItemAction by remember {
        mutableStateOf<ProfileInfoItemAction>(ProfileInfoItemAction.None)
    }

    val linkUtil = LinkUtil(LocalContext.current)

    LaunchedEffect(key1 = null) {
        uiEvent = StudentProfileScreenUiEvent.OnLoadProfile
    }

    // check for signing out from all devices
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(Const.Key.IS_SIGNED_OUT_FROM_ALL_DEVICE, false)
        ?.collectAsState()
        ?.value
        ?.let { isSignedOutFromAllDevices ->
            if (isSignedOutFromAllDevices) {
                // resetting tab to initial state
                viewModel.onClickTab(0)
            }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Boolean>(Const.Key.IS_SIGNED_OUT_FROM_ALL_DEVICE)
        }

    // check for photo upload
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>(Const.Key.URL, null)
        ?.collectAsState()
        ?.value
        ?.let { url ->
            viewModel.changeProfileImage(url)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>(Const.Key.URL)
        }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
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
            is StudentProfileScreenUiEvent.OnClickAction -> profileInfoItemAction = actionItem
            is StudentProfileScreenUiEvent.OnClickEdit -> viewModel.onClickEdit(selectedTabIndex)
            is StudentProfileScreenUiEvent.OnClickImage -> viewModel.onClickImage(url)
            is StudentProfileScreenUiEvent.OnEditBio -> viewModel.changeBio(newBio)
            is StudentProfileScreenUiEvent.OnCall -> linkUtil.callTo(phoneNumber)
            is StudentProfileScreenUiEvent.OnEmail -> linkUtil.sendEmail(email)
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
            is ProfileInfoItemAction.Link -> linkUtil.openBrowser(url)
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
                            Text(text = LocalContext.current.getString(R.string.txt_edit))
                        },
                        onClick = {
                            onUiEvent(
                                StudentProfileScreenUiEvent.OnClickEdit(
                                    displayState.selectedTabIndex,
                                )
                            )
                        },
                        icon = { Icon(Icons.Filled.Edit,"") },
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tabs = getStudentsTabs(context, profile.isSignedIn)
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
                        ImageVector.vectorResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        modifier = Modifier.clickable {
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
                Icon(
                    if (profile.isSignedIn) {
                        ImageVector.vectorResource(id = R.drawable.ic_sign_out)
                    } else {
                        Icons.Filled.Call
                    },
                    contentDescription = "Action button",
                )
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
            tabs = getStudentsTabs(context, profile.isSignedIn),
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getStudentAcademicTabItems(context, profile).ProfileInfoListView {
                    onUiEvent(StudentProfileScreenUiEvent.OnClickAction(it.action))
                }
                1 -> getStudentConnectTabItems(context, profile).ProfileInfoListView {
                    onUiEvent(StudentProfileScreenUiEvent.OnClickAction(it.action))
                }
                2 -> if (profile.isSignedIn) {
                    getStudentOptionTabItems(context).ProfileInfoListView {
                        onUiEvent(StudentProfileScreenUiEvent.OnClickAction(it.action))
                    }
                }
            }
        }
    }
}

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
    messageState?.Handle(onUiEvent)
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
    val context = LocalContext.current
    when (this) {
        is StudentProfileScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is StudentProfileScreenState.DisplayState.MessageState.InputBio -> {
            ShowInputDialog(
                title = context.getString(R.string.txt_change_bio),
                label = context.getString(R.string.hint_bio),
                input = currentBio,
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                maxLength = 150,
                confirmButtonText = context.getString(R.string.txt_update),
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
                title = context.getString(R.string.txt_title_call),
                message = context.getString(R.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = context.getString(R.string.txt_call),
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
                title = context.getString(R.string.txt_title_email),
                message = context.getString(R.string.txt_msg_email).plus(" $email"),
                confirmButtonText = context.getString(R.string.txt_email),
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
                title = context.getString(R.string.txt_sign_out),
                message = context.getString(R.string.msg_sign_out),
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
    navController: NavHostController,
    onUiEvent: (StudentProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is StudentProfileScreenState.NavigationState.ImageUploadScreen -> {
            navController.navigate(
                NavItem.ImageUpload.route
                    .plus("?${NavParam.USER_ID}=$userId")
                    .plus("&${NavParam.USER_TYPE}=${userType.type}"),
            )
        }
        is StudentProfileScreenState.NavigationState.ChangePasswordScreen -> {
            navController.navigate(NavItem.ChangePassword.route)
        }
        is StudentProfileScreenState.NavigationState.DownloadCvScreen -> {
            navController.navigate(
                NavItem.CvDownload.route
                    .plus("?${NavParam.USER_ID}=$userId")
                    .plus("&${NavParam.USER_TYPE}=${userType.type}")
                    .plus("&${NavParam.URL}=${url}"),
            )
        }
        is StudentProfileScreenState.NavigationState.UploadCvScreen -> {
            navController.navigate(
                NavItem.CvUpload.route
                    .plus("?${NavParam.USER_ID}=$userId")
                    .plus("&${NavParam.USER_TYPE}=${userType.type}"),
            )
        }
        is StudentProfileScreenState.NavigationState.MyBloodDonationListScreen -> {
            navController.navigate(
                NavItem.MyBloodDonationList.route
                    .plus("?${NavParam.USER_ID}=$userId")
                    .plus("&${NavParam.USER_TYPE}=${userType.type}"),
            )
        }
        is StudentProfileScreenState.NavigationState.MyCheckInListScreen -> {
            navController.navigate(
                NavItem.MyCheckInList.route
                    .plus("?${NavParam.USER_ID}=$userId")
                    .plus("&${NavParam.USER_TYPE}=${userType.type}"),
            )
        }
        is StudentProfileScreenState.NavigationState.MyDeviceListScreen -> {
            navController.navigate(NavItem.MyDeviceList.route)
        }
        is StudentProfileScreenState.NavigationState.StudentProfileEditScreen -> {
            navController.navigate(
                NavItem.StudentProfileEdit.route.plus("/${userId}/${action.mode}"),
            )
        }
        is StudentProfileScreenState.NavigationState.DeleteAccountScreen -> {
            navController.navigate(NavItem.DeleteAccount.route)
        }
        is StudentProfileScreenState.NavigationState.ImagePreviewScreen -> {
            navController.navigate(NavItem.ImagePreview.route.plus("/$encodedImageUrl"))
        }
    }
    onUiEvent(StudentProfileScreenUiEvent.NavigationConsumed)
}