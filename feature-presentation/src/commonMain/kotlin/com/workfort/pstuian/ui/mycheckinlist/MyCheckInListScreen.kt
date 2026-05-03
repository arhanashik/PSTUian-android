package com.workfort.pstuian.ui.mycheckinlist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInItemBottomSheet
import com.workfort.pstuian.ui.mycheckinlist.composable.MyCheckInListScreenContent
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInMessageState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_delete_permanent
import pstuian.feature_presentation.generated.resources.txt_delete
import pstuian.feature_presentation.generated.resources.txt_retry
import pstuian.feature_presentation.generated.resources.txt_update

@Composable
fun MyCheckInListScreen(
    viewModel: MyCheckInListViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    MyCheckInListScreenContent(uiState, snackbarHostState, viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: MyCheckInMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is MyCheckInMessageState.ShowDetails -> {
                MyCheckInItemBottomSheet(
                    item = it.item,
                    onClickChangePrivacy = { privacy ->
                        onMessageHandled()
                        it.onClickChangePrivacy(privacy)
                    },
                    onClickDelete = {
                        onMessageHandled()
                        it.onClickDelete()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is MyCheckInMessageState.ConfirmPrivacyChange -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_update),
                    message = "Are you surely want to change the privacy?",
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is MyCheckInMessageState.ConfirmDelete -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_delete),
                    message = stringResource(Res.string.msg_delete_permanent),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is MyCheckInMessageState.Success -> {
                HandleSnackbar(
                    message = it.message,
                    snackbarHostState = snackbarHostState,
                    onSnackbarShown = onMessageHandled,
                )
            }
            is MyCheckInMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    dismissButtonText = stringResource(Res.string.txt_retry),
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: MyCheckInNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is MyCheckInNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}
