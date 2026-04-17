package com.workfort.pstuian.ui.myblooddonationlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppBarIconButton
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.myblooddonationlist.composable.MyBloodDonationListContentPanel
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListMessageState
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListNavigationState
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiEvent
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_create_new
import pstuian.feature_presentation.generated.resources.txt_my_donation_list
import pstuian.feature_presentation.generated.resources.txt_retry

@Composable
fun MyBloodDonationListScreen(viewModel: MyBloodDonationListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    MyBloodDonationListScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled, viewModel::onUiEvent)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyBloodDonationListScreenContent(
    uiState: MyBloodDonationListUiState,
    onUiEvent: (MyBloodDonationListUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_my_donation_list),
                navigation = { onUiEvent(MyBloodDonationListUiEvent.BackClicked) },
                actions = {
                    AppBarIconButton(
                        icon = Icons.Filled.Refresh,
                        onClick = {
                            onUiEvent(MyBloodDonationListUiEvent.LoadList(refresh = true))
                        }
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = fabButtonExpanded,
                text = { Text(text = stringResource(Res.string.txt_create_new)) },
                onClick = {
                    onUiEvent(MyBloodDonationListUiEvent.CreateRequestClicked)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "",
                    )
                },
            )
        },
    ) {
        MyBloodDonationListContentPanel(uiState, onUiEvent)
    }
}

@Composable
private fun HandleMessageState(
    message: MyBloodDonationListMessageState?,
    onMessageHandled: () -> Unit,
    onUiEvent: (MyBloodDonationListUiEvent) -> Unit,
) {
    message?.let {
        when (it) {
            is MyBloodDonationListMessageState.ConfirmDelete -> {
                ShowConfirmationDialog(
                    message = stringResource(Res.string.msg_delete_permanent),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = { onMessageHandled() }
                )
            }
            is MyBloodDonationListMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is MyBloodDonationListMessageState.Success -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = { onMessageHandled() }
                )
            }
            is MyBloodDonationListMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = { onMessageHandled() }
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: MyBloodDonationListNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is MyBloodDonationListNavigationState.GoBack -> navigator?.goBack()
                is MyBloodDonationListNavigationState.GoToCreateBloodDonationRequest -> {
                    navigator?.navigateTo(AppScreen.BloodDonationRequestCreate)
                }
                is MyBloodDonationListNavigationState.GoToEditBloodDonationRequest -> {
                    navigator?.navigateTo(
                        AppScreen.BloodDonationRequestEdit(it.donationId),
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
