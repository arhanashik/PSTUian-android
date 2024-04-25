package com.workfort.pstuian.app.ui.checkinlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.DotView
import com.workfort.pstuian.app.ui.common.component.LoadAsyncImage
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.component.isLastItemVisible
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.util.helper.LinkUtil


@Composable
fun CheckInListScreen(
    modifier: Modifier = Modifier,
    viewModel: CheckInListViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<CheckInListScreenUiEvent>(CheckInListScreenUiEvent.None)
    }

    val linkUtil = LinkUtil(LocalContext.current)

    // check for location picker
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>(Const.Key.LOCATION, null)
        ?.collectAsState()
        ?.value
        ?.let { locationId ->
            viewModel.onChangeLocation(locationId)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Int>(Const.Key.LOCATION)
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
            is CheckInListScreenUiEvent.None -> Unit
            is CheckInListScreenUiEvent.OnLoadCheckInListList ->
                viewModel.loadCheckInList(isRefresh)
            is CheckInListScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is CheckInListScreenUiEvent.OnClickItem -> viewModel.onClickItem(item)
            is CheckInListScreenUiEvent.OnClickCall -> viewModel.onClickCall(phoneNumber)
            is CheckInListScreenUiEvent.OnClickChangeLocation -> viewModel.onClickChangeLocation()
            is CheckInListScreenUiEvent.OnClickCheckInList -> viewModel.onClickCheckIn()
            is CheckInListScreenUiEvent.OnCall -> linkUtil.callTo(phoneNumber)
            is CheckInListScreenUiEvent.OnCheckIn -> viewModel.checkIn(locationId)
            is CheckInListScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is CheckInListScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = CheckInListScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: CheckInListScreenState.DisplayState,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val isLastItemVisible by remember { derivedStateOf { listState.isLastItemVisible } }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(CheckInListScreenUiEvent.OnLoadCheckInListList(isRefresh = false))
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = LocalContext.current.getString(R.string.label_check_in_screen),
                onClickBack = {
                    onUiEvent(CheckInListScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            displayState.checkInLocation?.let { location ->
                CheckInListHeaderView(
                    checkInLocation = location,
                    onClickChangeLocation = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickChangeLocation)
                    },
                    onClickCheckIn = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickCheckInList)
                    },
                )
            }
            displayState.checkInListState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<CheckInEntity>.CheckInListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClickItem: (CheckInEntity) -> Unit,
    onClickCall: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@CheckInListView) { item ->
            CheckInListItemView(
                item = item,
                onClickItem = { onClickItem(item) },
                onClickCall = { item.phone?.let(onClickCall) }
            )
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun CheckInListItemView(
    item: CheckInEntity,
    onClickItem: () -> Unit,
    onClickCall: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                LoadAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    url = item.imageUrl,
                    placeholder = R.drawable.img_placeholder_profile,
                    contentScale = ContentScale.Crop,
                )
                DotView(modifier = Modifier.padding(16.dp))
                item.phone?.let {
                    IconButton(
                        modifier = Modifier.align(Alignment.BottomStart),
                        onClick = { onClickCall() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Icon",
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                TitleTextSmall(text = item.name, fontSize = 16.sp)
                Text(text = item.batch, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun CheckInListScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun CheckInListScreenState.DisplayState.CheckInListState.Handle(
    modifier: Modifier,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is CheckInListScreenState.DisplayState.CheckInListState.None -> Unit
        is CheckInListScreenState.DisplayState.CheckInListState.Available -> {
            if (checkInList.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        AnimatedEmptyView(modifier = Modifier.width(LottieAnimation.errorWidth))
                    }
                }
            } else {
                checkInList.CheckInListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onClickItem = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickItem(it))
                    },
                    onClickCall = {
                        onUiEvent(CheckInListScreenUiEvent.OnClickCall(it))
                    },
                )
            }
        }
        is CheckInListScreenState.DisplayState.CheckInListState.Error -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun CheckInListHeaderView(
    checkInLocation: CheckInLocationEntity,
    onClickChangeLocation: () -> Unit,
    onClickCheckIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 14.sp)) {
                append("Showing peoples in ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                append(checkInLocation.name)
            }
        }
        Text(text = annotatedString)
        Row {
            AssistChip(
                onClick = { onClickChangeLocation() },
                label = { Text("Change Location") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Change Location Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                shape = CircleShape,
            )
            Spacer(modifier = Modifier.padding(start = 8.dp))
            AssistChip(
                onClick = { onClickCheckIn() },
                label = { Text("Check In") },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Check In Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                shape = CircleShape,
            )
        }
    }
}

@Composable
private fun CheckInListScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is CheckInListScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is CheckInListScreenState.DisplayState.MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Notifications,
                title = context.getString(R.string.txt_title_call),
                message = context.getString(R.string.txt_msg_call).plus(" $phoneNumber"),
                confirmButtonText = context.getString(R.string.txt_call),
                onConfirm = {
                    onUiEvent(CheckInListScreenUiEvent.OnCall(phoneNumber))
                },
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CheckInListScreenState.DisplayState.MessageState.ConfirmCheckIn -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Notifications,
                message = context.getString(R.string.msg_confirm_check_in)
                    .plus(" ${location.name}"),
                confirmButtonText = context.getString(R.string.txt_check_in),
                onConfirm = {
                    onUiEvent(CheckInListScreenUiEvent.OnCheckIn(location.id))
                },
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CheckInListScreenState.DisplayState.MessageState.Success -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
        is CheckInListScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(CheckInListScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun CheckInListScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (CheckInListScreenUiEvent) -> Unit,
) {
    when (this) {
        is CheckInListScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is CheckInListScreenState.NavigationState.LocationPickerScreen -> {
            navController.navigate(NavItem.LocationPicker.route)
        }
        is CheckInListScreenState.NavigationState.ProfileScreen -> {
            navController.navigate(
                when (userType) {
                    UserType.STUDENT -> NavItem.StudentProfile.route.plus("/$userId")
                    UserType.TEACHER -> NavItem.TeacherProfile.route.plus("/$userId")
                },
            )
        }
    }
    onUiEvent(CheckInListScreenUiEvent.NavigationConsumed)
}