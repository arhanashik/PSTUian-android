package com.workfort.pstuian.app.ui.common.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.home.HomeScreenState
import com.workfort.pstuian.common.component.AnimatedErrorView
import com.workfort.pstuian.common.component.ErrorText
import com.workfort.pstuian.common.component.FacultyView
import com.workfort.pstuian.common.component.LoadAsyncUserImage
import com.workfort.pstuian.common.component.ShimmerBox
import com.workfort.pstuian.common.component.ShowConfirmationDialog
import com.workfort.pstuian.common.component.ShowErrorDialog
import com.workfort.pstuian.common.component.SliderView
import com.workfort.pstuian.common.component.TitleText
import com.workfort.pstuian.common.component.TitleTextMedium
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.app.ui.commonmodel.home.HomeViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name
import pstuian.feature_presentation.generated.resources.label_faculties
import pstuian.feature_presentation.generated.resources.label_information_corner
import pstuian.feature_presentation.generated.resources.label_options
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_sign_in_required
import pstuian.feature_presentation.generated.resources.msg_sign_in_required
import pstuian.feature_presentation.generated.resources.txt_notification
import pstuian.feature_presentation.generated.resources.msg_request_notification_permission
import pstuian.feature_presentation.generated.resources.txt_allow
import pstuian.feature_presentation.generated.resources.label_are_you_sure
import pstuian.feature_presentation.generated.resources.data_clear_message

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
    val screenState by viewModel.homeScreenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<HomeUiEvent>(HomeUiEvent.None)
    }
    var selectedActionItem by remember {
        mutableStateOf<ActionItem?>(null)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = HomeUiEvent.LoadInitialData
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(
            navigateToSignIn = navigateToSignIn,
            navigateToProfile = navigateToProfile,
            navigateToNotification = navigateToNotification,
            navigateToFaculty = navigateToFaculty,
            navigateToImagePreview = navigateToImagePreview,
            navigateToContactUs = navigateToContactUs,
            navigateToDonors = navigateToDonors,
            navigateToBloodDonationRequest = navigateToBloodDonationRequest,
            navigateToCheckIn = navigateToCheckIn,
            navigateToDonate = navigateToDonate,
            navigateToSettings = navigateToSettings,
        ) {
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
                requestNotificationPermission()
            }
            is HomeUiEvent.OnClearData -> viewModel.clearAllData()
            is HomeUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is HomeUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = HomeUiEvent.None
    }

    selectedActionItem?.action?.let {
        when (it) {
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TitleText(text = stringResource(Res.string.app_name))
            Spacer(modifier = Modifier.weight(1f))
            // load signed in user
            displayState.profileState.Handle(onUiEvent)
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Icon(
                imageVector = Icons.Default.NotificationsActive,
                contentDescription = "Notification",
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
                text = stringResource(Res.string.label_faculties),
                color = Color.Black,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            displayState.facultyState.Handle(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(vertical = 8.dp),
                onUiEvent = onUiEvent,
            )
        }
        // information corner
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            TitleTextMedium(
                text = stringResource(Res.string.label_information_corner),
                color = Color.Black,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            informationItems.forEach { item ->
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
        }
        // options
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
            TitleTextMedium(
                text = stringResource(Res.string.label_options),
                color = Color.Black,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            optionsItems.forEach { item ->
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
                text = stringResource(item.title),
                modifier = Modifier
                    .weight(0.6f)
                    .padding(start = 8.dp),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .padding(8.dp),
                )
            }
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
                text = stringResource(item.title),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .weight(0.3f),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.3f),
                )
            }
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
    when (this) {
        is HomeScreenState.DisplayState.ProfileState.None -> Unit
        is HomeScreenState.DisplayState.ProfileState.Loading -> Unit
        is HomeScreenState.DisplayState.ProfileState.Available -> {
            val imageUrl = when(val u = user) {
                is StudentEntity -> u.imageUrl
                is TeacherEntity -> u.imageUrl
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
                Text(text = stringResource(Res.string.txt_sign_in))
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
    when (this) {
        is HomeScreenState.DisplayState.MessageState.SignInNecessary -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_in_required),
                message = stringResource(Res.string.msg_sign_in_required),
                confirmButtonText = stringResource(Res.string.txt_sign_in),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnSignIn)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is HomeScreenState.DisplayState.MessageState.NotificationPermission -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Notifications,
                title = stringResource(Res.string.txt_notification),
                message = stringResource(Res.string.msg_request_notification_permission),
                confirmButtonText = stringResource(Res.string.txt_allow),
                onConfirm = {
                    onUiEvent(HomeUiEvent.OnRequestNotificationPermission)
                },
                onDismiss = { onUiEvent(HomeUiEvent.MessageConsumed) },
            )
        }
        is HomeScreenState.DisplayState.MessageState.ClearAllData -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.label_are_you_sure),
                message = stringResource(Res.string.data_clear_message),
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
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (this) {
        is HomeScreenState.NavigationState.SplashScreen -> {
            // This is handled by a splash route or similar in KMP
        }
        is HomeScreenState.NavigationState.SignInScreen -> {
            navigateToSignIn()
        }
        is HomeScreenState.NavigationState.GoToProfileScreen -> {
            navigateToProfile(userType, userId)
        }
        is HomeScreenState.NavigationState.NotificationScreen -> {
            navigateToNotification()
        }
        is HomeScreenState.NavigationState.FacultyScreen -> {
            navigateToFaculty(faculty)
        }
        is HomeScreenState.NavigationState.ImagePreviewScreen -> {
            navigateToImagePreview(encodedImageUrl)
        }
        is HomeScreenState.NavigationState.ContactUsScreen -> {
            navigateToContactUs()
        }
        is HomeScreenState.NavigationState.DonorsScreen -> {
            navigateToDonors()
        }
        is HomeScreenState.NavigationState.BloodDonationRequestScreen -> {
            navigateToBloodDonationRequest()
        }
        is HomeScreenState.NavigationState.CheckInScreen -> {
            navigateToCheckIn()
        }
        is HomeScreenState.NavigationState.DonateScreen -> {
            navigateToDonate()
        }
        is HomeScreenState.NavigationState.SettingsScreen -> {
            navigateToSettings()
        }
    }
    onUiEvent(HomeUiEvent.NavigationConsumed)
}
