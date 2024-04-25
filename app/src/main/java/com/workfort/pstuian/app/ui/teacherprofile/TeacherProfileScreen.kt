package com.workfort.pstuian.app.ui.teacherprofile

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
import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.util.helper.LinkUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun TeacherProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: TeacherProfileViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<TeacherProfileScreenUiEvent>(TeacherProfileScreenUiEvent.None)
    }
    var profileInfoItemAction by remember {
        mutableStateOf<ProfileInfoItemAction>(ProfileInfoItemAction.None)
    }

    val linkUtil = LinkUtil(LocalContext.current)

    LaunchedEffect(key1 = null) {
        uiEvent = TeacherProfileScreenUiEvent.OnLoadProfile
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
            is TeacherProfileScreenUiEvent.OnCall -> linkUtil.callTo(phoneNumber)
            is TeacherProfileScreenUiEvent.OnEmail -> linkUtil.sendEmail(email)
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
            is ProfileInfoItemAction.Link -> linkUtil.openBrowser(url)
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
                            Text(text = LocalContext.current.getString(R.string.txt_edit))
                        },
                        onClick = {
                            onUiEvent(
                                TeacherProfileScreenUiEvent.OnClickEdit(
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
    profile: TeacherProfile,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tabs = getTeacherTabs(context, profile.isSignedIn)
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
                        ImageVector.vectorResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        modifier = Modifier.clickable {
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
                0 -> getTeacherAcademicTabItems(context, profile).ProfileInfoListView {
                    onUiEvent(TeacherProfileScreenUiEvent.OnClickAction(it.action))
                }
                1 -> getTeacherConnectTabItems(context, profile).ProfileInfoListView {
                    onUiEvent(TeacherProfileScreenUiEvent.OnClickAction(it.action))
                }
                2 -> getTeacherOptionTabItems(context).ProfileInfoListView {
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
    messageState?.Handle(onUiEvent)
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
    val context = LocalContext.current
    when (this) {
        is TeacherProfileScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is TeacherProfileScreenState.DisplayState.MessageState.InputBio -> {
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
                title = context.getString(R.string.txt_title_call),
                message = context.getString(R.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = context.getString(R.string.txt_call),
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
                title = context.getString(R.string.txt_title_email),
                message = context.getString(R.string.txt_msg_email).plus(" $email"),
                confirmButtonText = context.getString(R.string.txt_email),
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
                title = context.getString(R.string.txt_sign_out),
                message = context.getString(R.string.msg_sign_out),
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
private fun TeacherProfileScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (TeacherProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is TeacherProfileScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is TeacherProfileScreenState.NavigationState.ImageUploadScreen -> {
            navController.navigate(
                NavItem.ImageUpload.route
                    .plus("?${NavParam.USER_ID}=$userId")
                    .plus("&${NavParam.USER_TYPE}=${userType.type}"),
            )
        }
        is TeacherProfileScreenState.NavigationState.ChangePasswordScreen -> {
            navController.navigate(NavItem.ChangePassword.route)
        }
        is TeacherProfileScreenState.NavigationState.MyDeviceListScreen -> {
            navController.navigate(NavItem.MyDeviceList.route)
        }
        is TeacherProfileScreenState.NavigationState.TeacherProfileEditScreen -> {
            navController.navigate(
                NavItem.TeacherProfileEdit.route.plus("/${userId}/${action.mode}"),
            )
        }
        is TeacherProfileScreenState.NavigationState.DeleteAccountScreen -> {
            navController.navigate(NavItem.DeleteAccount.route)
        }
        is TeacherProfileScreenState.NavigationState.ImagePreviewScreen -> {
            navController.navigate(
                NavItem.ImagePreview.route.plus("/$encodedImageUrl"),
            )
        }
    }
    onUiEvent(TeacherProfileScreenUiEvent.NavigationConsumed)
}