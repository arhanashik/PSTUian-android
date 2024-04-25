package com.workfort.pstuian.app.ui.donors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AnimatedEmptyView
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.ShowInfoDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall
import com.workfort.pstuian.app.ui.common.theme.LottieAnimation
import com.workfort.pstuian.model.DonorEntity
import kotlinx.coroutines.delay


@Composable
fun DonorsScreen(
    modifier: Modifier = Modifier,
    viewModel: DonorsViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<DonorsScreenUiEvent>(DonorsScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = DonorsScreenUiEvent.OnLoadData
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
            is DonorsScreenUiEvent.None -> Unit
            is DonorsScreenUiEvent.OnLoadData -> viewModel.loadDonors()
            is DonorsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is DonorsScreenUiEvent.OnClickItem -> viewModel.onClickItem(item)
            is DonorsScreenUiEvent.OnClickDonate -> viewModel.onClickDonate()
            is DonorsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is DonorsScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = DonorsScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: DonorsScreenState.DisplayState,
    onUiEvent: (DonorsScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_donors_screen),
                actionIcon = Icons.Filled.Refresh,
                onClickBack = {
                    onUiEvent(DonorsScreenUiEvent.OnClickBack)
                },
                onClickAction = {
                    onUiEvent(DonorsScreenUiEvent.OnLoadData)
                },
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                ExtendedFloatingActionButton(
                    expanded = fabButtonExpanded,
                    text = {  Text(text = context.getString(R.string.txt_donate)) },
                    onClick = {
                        onUiEvent(DonorsScreenUiEvent.OnClickDonate)
                    },
                    icon = {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.ic_hand_heart),
                            "",
                        )
                    },
                    shape = CircleShape,
                )
            }
        },
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            displayState.donorListState.Handle(modifier, onUiEvent)
        }
    }
}

@Composable
private fun List<DonorEntity>.DonorListView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onUiEvent: (DonorsScreenUiEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(this@DonorListView) { item ->
            DonorListItemView(
                item = item,
                onClickItem = {
                    onUiEvent(DonorsScreenUiEvent.OnClickItem(it))
                },
            )
        }
        if (isLoading) {
            item {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DonorListItemView(
    item: DonorEntity,
    onClickItem: (DonorEntity) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem(item) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(
                ImageVector.vectorResource(id = R.drawable.ic_account_heart_outline),
                contentDescription = null,
                tint = Color.Gray,
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                TitleTextSmall(text = item.name ?: "Donation Info")
                item.info?.let { Text(text = it) }
                Text(text = "Reference: ${item.reference}")
            }
        }
    }
}

@Composable
private fun DonorsScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (DonorsScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun DonorsScreenState.DisplayState.DonorListState.Handle(
    modifier: Modifier,
    onUiEvent: (DonorsScreenUiEvent) -> Unit,
) {
    when (this) {
        is DonorsScreenState.DisplayState.DonorListState.None -> Unit
        is DonorsScreenState.DisplayState.DonorListState.Available -> {
            if (donorList.isEmpty()) {
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
                donorList.DonorListView(
                    modifier = modifier,
                    isLoading = isLoading,
                    onUiEvent = onUiEvent,
                )
            }
        }
        is DonorsScreenState.DisplayState.DonorListState.Error -> {
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
private fun DonorsScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (DonorsScreenUiEvent) -> Unit,
) {
    when (this) {
        is DonorsScreenState.DisplayState.MessageState.ShowDetails -> {
            val message = "Email: ${item.email}\n${item.info}\nReference${item.reference}"
            ShowInfoDialog(
                icon = ImageVector.vectorResource(id = R.drawable.ic_account_heart_outline),
                title = item.name ?: "Donation Info",
                message = message,
                onDismiss = {
                    onUiEvent(DonorsScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun DonorsScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (DonorsScreenUiEvent) -> Unit,
) {
    when (this) {
        is DonorsScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is DonorsScreenState.NavigationState.DonateScreen -> {
            navController.navigate(NavItem.Donate.route)
        }
    }
    onUiEvent(DonorsScreenUiEvent.NavigationConsumed)
}