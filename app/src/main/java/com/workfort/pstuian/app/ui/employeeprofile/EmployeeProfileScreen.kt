package com.workfort.pstuian.app.ui.employeeprofile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.app.ui.common.component.ProfileInfoListView
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.TabView
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.model.EmployeeProfile
import com.workfort.pstuian.model.ProfileInfoItemAction
import com.workfort.pstuian.util.helper.LinkUtil
import kotlinx.coroutines.launch


@Composable
fun EmployeeProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: EmployeeProfileViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<EmployeeProfileScreenUiEvent>(EmployeeProfileScreenUiEvent.None)
    }
    var profileInfoItemAction by remember {
        mutableStateOf<ProfileInfoItemAction>(ProfileInfoItemAction.None)
    }

    val linkUtil = LinkUtil(LocalContext.current)

    LaunchedEffect(key1 = null) {
        uiEvent = EmployeeProfileScreenUiEvent.OnLoadProfile
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
            is EmployeeProfileScreenUiEvent.None -> Unit
            is EmployeeProfileScreenUiEvent.OnLoadProfile -> viewModel.loadProfile()
            is EmployeeProfileScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is EmployeeProfileScreenUiEvent.OnClickCall -> viewModel.onClickCall()
            is EmployeeProfileScreenUiEvent.OnClickTab -> viewModel.onClickTab(index)
            is EmployeeProfileScreenUiEvent.OnClickImage -> viewModel.onClickImage(url)
            is EmployeeProfileScreenUiEvent.OnClickBio -> viewModel.onClickBio()
            is EmployeeProfileScreenUiEvent.OnClickAction -> profileInfoItemAction = actionItem
            is EmployeeProfileScreenUiEvent.OnCall -> linkUtil.callTo(phoneNumber)
            is EmployeeProfileScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is EmployeeProfileScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = EmployeeProfileScreenUiEvent.None
    }

    with(profileInfoItemAction) {
        when (this) {
            is ProfileInfoItemAction.None -> Unit
            is ProfileInfoItemAction.Edit -> Unit
            is ProfileInfoItemAction.Call -> viewModel.onClickCall()
            is ProfileInfoItemAction.Email -> Unit
            is ProfileInfoItemAction.DownloadCv -> Unit
            is ProfileInfoItemAction.Link -> Unit
            is ProfileInfoItemAction.Password -> Unit
            is ProfileInfoItemAction.UploadCv -> Unit
            is ProfileInfoItemAction.BloodDonationList -> Unit
            is ProfileInfoItemAction.CheckInList -> Unit
            is ProfileInfoItemAction.SignedInDevices -> Unit
            is ProfileInfoItemAction.DeleteAccount -> Unit
        }
        profileInfoItemAction = ProfileInfoItemAction.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: EmployeeProfileScreenState.DisplayState,
    onUiEvent: (EmployeeProfileScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        displayState.profileState.Handle(modifier.padding(innerPadding), onUiEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfileView(
    modifier: Modifier,
    profile: EmployeeProfile,
    onUiEvent: (EmployeeProfileScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val userImageUrl = profile.emplyee.imageUrl
    val scope = rememberCoroutineScope()
    val tabs = listOf(
        context.getString(R.string.txt_academic),
        context.getString(R.string.txt_connect),
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    LaunchedEffect(key1 = selectedTabIndex) {
        onUiEvent(EmployeeProfileScreenUiEvent.OnClickTab(selectedTabIndex))
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
                onClick = { onUiEvent(EmployeeProfileScreenUiEvent.OnClickBack) },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
            LoadAsyncUserImage(
                modifier = if (userImageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(EmployeeProfileScreenUiEvent.OnClickImage(userImageUrl))
                    }
                },
                url = userImageUrl,
                size = 96.dp,
            )
            IconButton(
                onClick = { onUiEvent(EmployeeProfileScreenUiEvent.OnClickCall) },
            ) {
                Icon(Icons.Filled.Call, contentDescription = "Action button")
            }
        }
        TitleTextSmall(
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            text = profile.emplyee.name,
        )
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
                0 -> getEmployeeAcademicTabItems(context, profile).ProfileInfoListView {
                    onUiEvent(EmployeeProfileScreenUiEvent.OnClickAction(it.action))
                }
                1 -> getEmployeeConnectTabItems(context, profile).ProfileInfoListView {
                    onUiEvent(EmployeeProfileScreenUiEvent.OnClickAction(it.action))
                }
            }
        }
    }
}

@Composable
private fun EmployeeProfileScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (EmployeeProfileScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun EmployeeProfileScreenState.DisplayState.ProfileState.Handle(
    modifier: Modifier,
    onUiEvent: (EmployeeProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is EmployeeProfileScreenState.DisplayState.ProfileState.None -> Unit
        is EmployeeProfileScreenState.DisplayState.ProfileState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
        is EmployeeProfileScreenState.DisplayState.ProfileState.Available -> {
            ProfileView(modifier, profile, onUiEvent)
        }
        is EmployeeProfileScreenState.DisplayState.ProfileState.Error -> {
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
private fun EmployeeProfileScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (EmployeeProfileScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is EmployeeProfileScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = context.getString(R.string.txt_title_call),
                message = context.getString(R.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = context.getString(R.string.txt_call),
                onConfirm = {
                    onUiEvent(EmployeeProfileScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(EmployeeProfileScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun EmployeeProfileScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (EmployeeProfileScreenUiEvent) -> Unit,
) {
    when (this) {
        is EmployeeProfileScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is EmployeeProfileScreenState.NavigationState.ImagePreviewScreen -> {
            navController.navigate(NavItem.ImagePreview.route.plus("/$encodedImageUrl"))
        }
    }
    onUiEvent(EmployeeProfileScreenUiEvent.NavigationConsumed)
}