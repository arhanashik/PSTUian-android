package com.workfort.pstuian.ui.locationpicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.featuredomain.appconstant.Const
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.common.composable.AnimatedEmptyView
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowSuccessDialog
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerNavigationState
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerUiEvent
import com.workfort.pstuian.ui.locationpicker.state.LocationPickerUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_search
import pstuian.feature_presentation.generated.resources.label_location_picker_screen
import pstuian.feature_presentation.generated.resources.txt_create_new_location

@Composable
fun LocationPickerScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationPickerViewModel,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LocationPickerScreenContent(
        modifier = modifier,
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is LocationPickerUiEvent.OnSearch -> viewModel.search(event.query, event.refresh)
                is LocationPickerUiEvent.OnAddLocation -> viewModel.createNewLocation(event.locationName)
                is LocationPickerUiEvent.OnClickBack -> viewModel.onClickBack()
                is LocationPickerUiEvent.OnClickAddLocation -> viewModel.onClickAddLocation()
                is LocationPickerUiEvent.OnClickLocation -> viewModel.onClickLocation(event.location)
                is LocationPickerUiEvent.MessageConsumed -> viewModel.messageConsumed()
                is LocationPickerUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
            }
        },
    )

    uiState.navigationState?.let { navigationState ->
        when (navigationState) {
            is LocationPickerNavigationState.GoBack -> {
                navigationState.selectedLocationId?.let { locationId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(Const.Key.LOCATION, locationId)
                }
                navController.popBackStack()
            }
        }
        viewModel.navigationConsumed()
    }
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationPickerScreenContent(
    modifier: Modifier,
    uiState: LocationPickerUiState,
    onUiEvent: (LocationPickerUiEvent) -> Unit,
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
        if (isLastItemVisible) {
            onUiEvent(LocationPickerUiEvent.OnSearch(searchQuery, refresh = false))
        }
    }

    LaunchedEffect(key1 = searchQuery) {
        onUiEvent(LocationPickerUiEvent.OnSearch(searchQuery, refresh = true))
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_location_picker_screen),
                navigation = {
                    onUiEvent(LocationPickerUiEvent.OnClickBack)
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            OutlinedTextInput(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                label = stringResource(Res.string.hint_search),
                value = searchQuery,
            ) {
                onChangeSearchQuery(it)
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            uiState.locationListState.Handle(modifier, onUiEvent)
        }
    }

    uiState.messageState?.Handle(onUiEvent)
}

@Composable
private fun List<CheckInLocation>.ListView(
    modifier: Modifier,
    isLoading: Boolean,
    onClick: (CheckInLocation) -> Unit,
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
    item: CheckInLocation,
    onClick: (CheckInLocation) -> Unit,
) {
    // TODO: MathUtil.prettyCount(item.count) needs KMP implementation or abstraction
    val checkInCountStr = "${item.count} check in"
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
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp)),
            tint = Color.Gray
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
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(8.dp)),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Column {
            TitleTextSmall(
                text = stringResource(Res.string.txt_create_new_location),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun LocationPickerUiState.LocationListState.Handle(
    modifier: Modifier,
    onUiEvent: (LocationPickerUiEvent) -> Unit,
) {
    when (this) {
        is LocationPickerUiState.LocationListState.None -> Unit
        is LocationPickerUiState.LocationListState.Available -> {
            if (locations.isEmpty()) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        CreateCheckInLocationItemView {
                            onUiEvent(LocationPickerUiEvent.OnClickAddLocation)
                        }
                        AnimatedEmptyView()
                    }
                }
            } else {
                locations.ListView(modifier = modifier, isLoading = isLoading) {
                    onUiEvent(LocationPickerUiEvent.OnClickLocation(it))
                }
            }
        }
        is LocationPickerUiState.LocationListState.Error -> {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CreateCheckInLocationItemView {
                    onUiEvent(LocationPickerUiEvent.OnClickAddLocation)
                }
                AnimatedErrorView()
            }
        }
    }
}

@Composable
private fun LocationPickerUiState.MessageState.Handle(
    onUiEvent: (LocationPickerUiEvent) -> Unit,
) {
    when (this) {
        is LocationPickerUiState.MessageState.ConfirmAddLocation -> {
            ShowConfirmationDialog(
                message = "Are you surely want to add a new location: $locationName",
                onConfirm = {
                    onUiEvent(LocationPickerUiEvent.MessageConsumed)
                    onUiEvent(LocationPickerUiEvent.OnAddLocation(locationName))
                },
                onDismiss = {
                    onUiEvent(LocationPickerUiEvent.MessageConsumed)
                }
            )
        }
        is LocationPickerUiState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                onConfirm = {
                    onUiEvent(LocationPickerUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(LocationPickerUiEvent.MessageConsumed)
                }
            )
        }
        is LocationPickerUiState.MessageState.Error -> {
            ShowErrorDialog(
                icon = Icons.Default.Notifications,
                message = message,
                onConfirm = {
                    onUiEvent(LocationPickerUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(LocationPickerUiEvent.MessageConsumed)
                }
            )
        }
    }
}
