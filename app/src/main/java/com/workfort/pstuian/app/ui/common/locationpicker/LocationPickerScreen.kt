package com.workfort.pstuian.app.ui.common.locationpicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.LoadAsyncImage
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.util.helper.MathUtil


@Composable
fun LocationPickerScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationPickerViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<LocationPickerScreenUiEvent>(LocationPickerScreenUiEvent.None)
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
            is LocationPickerScreenUiEvent.None -> Unit
            is LocationPickerScreenUiEvent.OnSearch -> viewModel.search(query, refresh)
            is LocationPickerScreenUiEvent.OnAddLocation -> viewModel.createNewLocation(locationName)
            is LocationPickerScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is LocationPickerScreenUiEvent.OnClickAddLocation -> viewModel.onClickAddLocation()
            is LocationPickerScreenUiEvent.OnClickLocation -> viewModel.onClickLocation(location)
            is LocationPickerScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is LocationPickerScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = LocationPickerScreenUiEvent.None
    }
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: LocationPickerScreenState.DisplayState,
    onUiEvent: (LocationPickerScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val (searchQuery, onChangeSearchQuery) = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val isLastItemVisible by remember {
        derivedStateOf {
            listState.isLastItemVisible
        }
    }

    LaunchedEffect(key1 = isLastItemVisible) {
        onUiEvent(LocationPickerScreenUiEvent.OnSearch(searchQuery, refresh = false))
    }

    LaunchedEffect(key1 = searchQuery) {
        onUiEvent(LocationPickerScreenUiEvent.OnSearch(searchQuery, refresh = true))
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = LocalContext.current.getString(R.string.label_location_picker_screen),
                onClickBack = {
                    onUiEvent(LocationPickerScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            OutlinedTextInput(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                label = LocalContext.current.getString(R.string.hint_search),
                value = searchQuery,
            ) {
                onChangeSearchQuery(it)
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            displayState.locationListState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<CheckInLocationEntity>.ListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClick: (CheckInLocationEntity) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@ListView) { item ->
            ListItemView(item) {
                onClick(it)
            }
        }
        if (isLoading) {
            item { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun ListItemView(
    item: CheckInLocationEntity,
    onClick: (CheckInLocationEntity) -> Unit,
) {
    val formattedCount = MathUtil.prettyCount(item.count)
    val checkInCountStr = "$formattedCount check in"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Check Icon",
            tint = Color.LightGray,
        )
        Spacer(modifier = Modifier.padding(start = 16.dp))
        LoadAsyncImage(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp)),
            url = item.imageUrl,
            placeholder = R.drawable.ic_location_placeholder,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            TitleTextSmall(text = item.name, fontSize = 14.sp)
            Text(text = checkInCountStr, fontSize = 12.sp)
        }
    }
}

@Composable
private fun CreateCheckInLocationItemView(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.AddCircle,
            contentDescription = "Check Icon",
            tint = Color.LightGray,
        )
        Spacer(modifier = Modifier.padding(start = 16.dp))
        Image(
            painterResource(id = R.drawable.ic_location_placeholder),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp)),
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            TitleTextSmall(
                text = LocalContext.current.getString(R.string.txt_create_new_location),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun LocationPickerScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (LocationPickerScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun LocationPickerScreenState.DisplayState.LocationListState.Handle(
    modifier: Modifier,
    onUiEvent: (LocationPickerScreenUiEvent) -> Unit,
) {
    when (this) {
        is LocationPickerScreenState.DisplayState.LocationListState.None -> Unit
        is LocationPickerScreenState.DisplayState.LocationListState.Available -> {
            if (locations.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        CreateCheckInLocationItemView {
                            onUiEvent(LocationPickerScreenUiEvent.OnClickAddLocation)
                        }
                        AnimatedEmptyView(modifier = Modifier.width(LottieAnimation.errorWidth))
                    }
                }
            } else {
                locations.ListView(modifier = modifier, isLoading = isLoading) {
                    onUiEvent(LocationPickerScreenUiEvent.OnClickLocation(it))
                }
            }
        }
        is LocationPickerScreenState.DisplayState.LocationListState.Error -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CreateCheckInLocationItemView {
                    onUiEvent(LocationPickerScreenUiEvent.OnClickAddLocation)
                }
                AnimatedErrorView(modifier = Modifier.width(LottieAnimation.errorWidth))
            }
        }
    }
}

@Composable
private fun LocationPickerScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (LocationPickerScreenUiEvent) -> Unit,
) {
    when (this) {
        is LocationPickerScreenState.DisplayState.MessageState.ConfirmAddLocation -> {
            ShowConfirmationDialog(
                message = "Are you surely want to add a new location: $locationName",
                onConfirm = {
                    onUiEvent(LocationPickerScreenUiEvent.MessageConsumed)
                    onUiEvent(LocationPickerScreenUiEvent.OnAddLocation(locationName))
                },
                onDismiss = {
                    onUiEvent(LocationPickerScreenUiEvent.MessageConsumed)
                }
            )
        }
        is LocationPickerScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                onConfirm = {
                    onUiEvent(LocationPickerScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(LocationPickerScreenUiEvent.MessageConsumed)
                }
            )
        }
        is LocationPickerScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                icon = Icons.Default.Notifications,
                message = message,
                onConfirm = {
                    onUiEvent(LocationPickerScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(LocationPickerScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun LocationPickerScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (LocationPickerScreenUiEvent) -> Unit,
) {
    when (this) {
        is LocationPickerScreenState.NavigationState.GoBack -> {
            selectedLocationId?.let { locationId ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(Const.Key.LOCATION, locationId)
            }
            navController.popBackStack()
        }
    }
    onUiEvent(LocationPickerScreenUiEvent.NavigationConsumed)
}