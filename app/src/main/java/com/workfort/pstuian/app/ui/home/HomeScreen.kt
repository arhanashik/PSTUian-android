package com.workfort.pstuian.app.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.ErrorText
import com.workfort.pstuian.app.ui.common.component.FacultyView
import com.workfort.pstuian.app.ui.common.component.LoadAsyncUserImage
import com.workfort.pstuian.app.ui.common.component.ShimmerBox
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.SliderView
import com.workfort.pstuian.app.ui.common.component.TitleText
import com.workfort.pstuian.app.ui.common.component.TitleTextMedium
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.PlayStoreUtil

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val screenState by viewModel.homeScreenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<HomeUiEvent>(HomeUiEvent.LoadInitialData)
    }
    var selectedActionItem by remember {
        mutableStateOf<ActionItem?>(null)
    }
    val playStoreUtil = PlayStoreUtil(LocalContext.current)
    val linkUtil = LinkUtil(LocalContext.current)

    // launcher to request permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) {
        // boolean value is isGranted value
    }

    LaunchedEffect(key1 = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheckResult = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS,
            )
            if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
               viewModel.showNotificationPermissionConfirmation()
            }
        }
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
            is HomeUiEvent.None -> Unit
            is HomeUiEvent.LoadInitialData -> viewModel.loadInitialData()
            is HomeUiEvent.GetSliders -> viewModel.getSliders()
            is HomeUiEvent.GetFaculties -> viewModel.getFaculties()
            is HomeUiEvent.GetUserProfile -> viewModel.getUserProfile()
            is HomeUiEvent.OnClickSignIn -> viewModel.onClickSignIn()
            is HomeUiEvent.OnClickUserProfile -> viewModel.onClickUserProfile()
            is HomeUiEvent.OnClickNotification -> viewModel.onClickNotification()
            is HomeUiEvent.OnScrollSlider -> viewModel.onScrollSlider(position)
            is HomeUiEvent.OnClickSlider -> viewModel.onClickSlider(slider)
            is HomeUiEvent.OnClickFaculty -> viewModel.onClickFaculty(faculty)
            is HomeUiEvent.OnClickActionItem -> selectedActionItem = actionItem
            is HomeUiEvent.OnSignIn -> viewModel.onClickSignIn()
            is HomeUiEvent.OnRequestNotificationPermission -> {
                viewModel.messageConsumed()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            is HomeUiEvent.OnClearData -> viewModel.clearAllData()
            is HomeUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is HomeUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = HomeUiEvent.None
    }

    selectedActionItem?.action?.let {
        when (it) {
            Action.AdmissionSupport -> linkUtil.openBrowser(NetworkConst.Remote.PSTU_WEBSITE)
            Action.Donors -> viewModel.onClickDonors()
            Action.VarsityWebsite -> linkUtil.openBrowser(NetworkConst.Remote.PSTU_WEBSITE)
            Action.ContactUs -> viewModel.onClickContactUs()
            Action.RequestBloodDonation -> viewModel.onClickRequestBloodDonation()
            Action.CheckIn -> viewModel.onClickCheckIn()
            Action.RateApp -> playStoreUtil.openStore()
            Action.ClearData -> viewModel.onClickClearData()
            Action.Settings -> viewModel.onClickSettings()
            Action.Donate -> viewModel.onClickDonate()
        }
        selectedActionItem = null
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeScreenContent(
    modifier: Modifier,
    displayState: HomeScreenState.DisplayState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    val context = LocalContext.current
    FlowRow(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TitleText(text = context.getString(R.string.app_name))
            Spacer(modifier = modifier.weight(1f))
            // load signed in user
            displayState.profileState.Handle(onUiEvent)
            Spacer(modifier = modifier.padding(start = 16.dp))
            Image(
                painterResource(R.drawable.ic_bell_filled),
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onUiEvent(HomeUiEvent.OnClickNotification)
                    },
            )
        }
        // slider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .layout { measurable, constraints ->
                    // removing the horizontal padding set by parent
                    val horizontalPadding = 16.dp.roundToPx()
                    // measure the composable with the padding*2 (left+right)
                    val placeable = measurable.measure(
                        constraints.offset(horizontal = horizontalPadding * 2),
                    )
                    // reset the width by removing the padding*2
                    layout(placeable.width - horizontalPadding * 2, placeable.height) {
                        // place the composable
                        placeable.place(-horizontalPadding, 0)
                    }
                }
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            displayState.sliderState.Handle(onUiEvent)
        }
        // faculties
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            TitleTextMedium(
                text = context.getString(R.string.label_faculties),
                color = Color.Black,
            )
        }
        displayState.facultyState.Handle(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(vertical = 8.dp),
            onUiEvent = onUiEvent,
        )
        // information corner
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            TitleTextMedium(
                text = context.getString(R.string.label_information_corner),
                color = Color.Black,
            )
        }
        getInformationItems(context).forEach { item ->
            InformationCornerView(
                item = item,
                modifier = Modifier
                    .fillMaxWidth(0.48f)
                    .height(84.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        onUiEvent(HomeUiEvent.OnClickActionItem(item))
                    },
            )
        }
        // options
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            TitleTextMedium(
                text = context.getString(R.string.label_options),
                color = Color.Black,
            )
        }
        getOptionsItems(context).forEach { item ->
            OptionView(
                item = item,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(84.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        onUiEvent(HomeUiEvent.OnClickActionItem(item))
                    },
            )
        }
    }
}

@Composable
private fun InformationCornerView(item: ActionItem, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.title,
                modifier = Modifier
                    .weight(0.6f)
                    .padding(start = 8.dp),
            )
            Image(
                painterResource(item.icon),
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f),
            )
        }
    }
}

@Composable
private fun OptionView(item: ActionItem, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                text = item.title,
            )
            Image(
                painterResource(item.icon),
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .weight(0.3f),
            )
        }
    }
}

@Composable
private fun HomeScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    HomeScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent = onUiEvent)
}

@Composable
private fun HomeScreenState.DisplayState.ProfileState.Handle(onUiEvent: (HomeUiEvent) -> Unit) {
    val context = LocalContext.current
    when (this) {
        is HomeScreenState.DisplayState.ProfileState.None -> Unit
        is HomeScreenState.DisplayState.ProfileState.Loading -> Unit
        is HomeScreenState.DisplayState.ProfileState.Available -> {
            val imageUrl = when(user) {
                is StudentEntity -> user.imageUrl
                is TeacherEntity -> user.imageUrl
                else -> null
            }
            LoadAsyncUserImage(
                modifier = Modifier.clickable {
                    onUiEvent(HomeUiEvent.OnClickUserProfile)
                },
                url = imageUrl,
                size = 24.dp,
            )
        }
        is HomeScreenState.DisplayState.ProfileState.Error -> {
            TextButton(onClick = { onUiEvent(HomeUiEvent.OnClickSignIn) }) {
                Text(text = context.getString(R.string.txt_sign_in))
            }
        }
    }
}

@Composable
private fun HomeScreenState.DisplayState.SliderState.Handle(
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (this) {
        is HomeScreenState.DisplayState.SliderState.None -> Unit
        is HomeScreenState.DisplayState.SliderState.Loading -> {
            ShimmerBox()
        }
        is HomeScreenState.DisplayState.SliderState.Available -> {
            SliderView(
                sliders = sliders,
                scrollPosition = scrollPosition,
                onScrollSlider = {
                    onUiEvent(HomeUiEvent.OnScrollSlider(it))
                },
                onClickSlider = {
                    onUiEvent(HomeUiEvent.OnClickSlider(it))
                },
            )
        }
        is HomeScreenState.DisplayState.SliderState.Error -> {
            AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidthSmall))
        }
    }
}

@Composable
private fun HomeScreenState.DisplayState.FacultyState.Handle(
    modifier: Modifier,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (this) {
        is HomeScreenState.DisplayState.FacultyState.None -> Unit
        is HomeScreenState.DisplayState.FacultyState.Loading -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Loading, please wait...")
            }
        }
        is HomeScreenState.DisplayState.FacultyState.Available -> {
            faculties.forEach { item ->
                FacultyView(
                    modifier = modifier
                        .clickable {
                            onUiEvent(HomeUiEvent.OnClickFaculty(item))
                        },
                    faculty = item,
                )
            }
        }
        is HomeScreenState.DisplayState.FacultyState.Error -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                ErrorText(text = message)
            }
        }
    }
}

@Composable
private fun HomeScreenState.DisplayState.MessageState.Handle(onUiEvent: (HomeUiEvent) -> Unit) {
    val context = LocalContext.current
    when (this) {
        is HomeScreenState.DisplayState.MessageState.SignInNecessary -> {
            ShowConfirmationDialog(
                title = context.getString(R.string.txt_sign_in_required),
                message = context.getString(R.string.msg_sign_in_required),
                confirmButtonText = context.getString(R.string.txt_sign_in),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnSignIn)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is HomeScreenState.DisplayState.MessageState.NotificationPermission -> {
            ShowConfirmationDialog(
                icon = ImageVector.vectorResource(id = R.drawable.ic_bell_filled),
                title = context.getString(R.string.txt_notification),
                message = context.getString(R.string.msg_request_notification_permission),
                confirmButtonText = context.getString(R.string.txt_allow),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnRequestNotificationPermission)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is HomeScreenState.DisplayState.MessageState.ClearAllData -> {
            ShowConfirmationDialog(
                title = context.getString(R.string.label_are_you_sure),
                message = context.getString(R.string.data_clear_message),
                onConfirm = {
                    onUiEvent(HomeUiEvent.MessageConsumed)
                    onUiEvent(HomeUiEvent.OnClearData)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is HomeScreenState.DisplayState.MessageState.ClearAllDataFailed -> {
            ShowErrorDialog(
                message = error,
                onConfirm = {
                    onUiEvent(HomeUiEvent.MessageConsumed)
                    onUiEvent(HomeUiEvent.OnClearData)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) }
            )
        }
    }
}

@Composable
private fun HomeScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (this) {
        is HomeScreenState.NavigationState.SplashScreen -> {
            navController.navigate(NavItem.Splash.route) {
                popUpTo(NavItem.Home.route) {
                    inclusive = true
                }
            }
        }
        is HomeScreenState.NavigationState.SignInScreen -> {
            navController.navigate(NavItem.SignIn.route)
        }
        is HomeScreenState.NavigationState.GoToProfileScreen -> {
            val route = when (userType) {
                UserType.STUDENT -> NavItem.StudentProfile.route
                UserType.TEACHER -> NavItem.TeacherProfile.route
            }
            navController.navigate(route.plus("/${userId}"))
        }
        is HomeScreenState.NavigationState.NotificationScreen -> {
            navController.navigate(NavItem.Notification.route)
        }
        is HomeScreenState.NavigationState.FacultyScreen -> {
            navController.navigate(
                NavItem.Faculty.route.plus("/${faculty.id}"),
            )
        }
        is HomeScreenState.NavigationState.ImagePreviewScreen -> {
            navController.navigate(NavItem.ImagePreview.route.plus("/$encodedImageUrl"))
        }
        is HomeScreenState.NavigationState.ContactUsScreen -> {
            navController.navigate(NavItem.ContactUs.route)
        }
        is HomeScreenState.NavigationState.DonorsScreen -> {
            navController.navigate(NavItem.Donors.route)
        }
        is HomeScreenState.NavigationState.BloodDonationRequestScreen -> {
            navController.navigate(NavItem.BloodDonationRequestList.route)
        }
        is HomeScreenState.NavigationState.CheckInScreen -> {
            navController.navigate(NavItem.CheckInList.route)
        }
        is HomeScreenState.NavigationState.DonateScreen -> {
            navController.navigate(NavItem.Donate.route)
        }
        is HomeScreenState.NavigationState.SettingsScreen -> {
            navController.navigate(NavItem.Settings.route)
        }
    }
    onUiEvent(HomeUiEvent.NavigationConsumed)
}