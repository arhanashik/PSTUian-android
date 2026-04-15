package com.workfort.pstuian.ui.teacherprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
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
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowInputDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.ui.teacherprofile.composable.TeacherProfileContentPanel
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileMessageState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileNavigationState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiEvent
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_bio
import pstuian.feature_presentation.generated.resources.msg_sign_out
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_bio
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_msg_email
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_title_call
import pstuian.feature_presentation.generated.resources.txt_title_email
import pstuian.feature_presentation.generated.resources.txt_update

@Composable
internal fun TeacherProfileScreen(viewModel: TeacherProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    TeacherProfileScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::messageHandled)
    HandleNavigationState(navigation, viewModel::navigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherProfileScreenContent(
    uiState: TeacherProfileUiState,
    onUiEvent: (TeacherProfileUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            if (uiState.isSignedIn && uiState.selectedTabIndex < 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    ExtendedFloatingActionButton(
                        expanded = fabButtonExpanded,
                        text = {
                            Text(text = stringResource(Res.string.txt_edit))
                        },
                        onClick = {
                            onUiEvent(
                                TeacherProfileUiEvent.EditClicked(
                                    uiState.selectedTabIndex,
                                )
                            )
                        },
                        icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        shape = CircleShape,
                    )
                }
            }
        },
    ) {
        TeacherProfileContentPanel(uiState, onUiEvent)
    }
}

@Composable
private fun HandleMessageState(
    message: TeacherProfileMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (message) {
            is TeacherProfileMessageState.Loading -> {
                ShowLoaderDialog(cancelable = message.cancelable)
            }
            is TeacherProfileMessageState.InputBio -> {
                ShowInputDialog(
                    title = stringResource(Res.string.txt_change_bio),
                    label = stringResource(Res.string.hint_bio),
                    input = message.currentBio,
                    singleLine = false,
                    minLines = 3,
                    maxLines = 5,
                    maxLength = 150,
                    confirmButtonText = stringResource(Res.string.txt_update),
                    onConfirm = { newBio ->
                        onMessageHandled()
                        message.onConfirm(newBio)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.CallConfirmation -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Call,
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${message.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        onMessageHandled()
                        message.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.EmailConfirmation -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Email,
                    title = stringResource(Res.string.txt_title_email),
                    message = stringResource(Res.string.txt_msg_email).plus(" ${message.email}"),
                    confirmButtonText = stringResource(Res.string.txt_email),
                    onConfirm = {
                        onMessageHandled()
                        message.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.ConfirmSignOut -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_sign_out),
                    message = stringResource(Res.string.msg_sign_out),
                    onConfirm = {
                        onMessageHandled()
                        message.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.Success -> {
                ShowSuccessDialog(
                    message = message.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.Error -> {
                ShowErrorDialog(
                    message = message.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: TeacherProfileNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is TeacherProfileNavigationState.GoBack -> navigator?.goBack()
                is TeacherProfileNavigationState.ImagePreviewScreen -> {
                    navigator?.navigateTo(AppScreen.ImagePreview(it.encodedImageUrl))
                }
                is TeacherProfileNavigationState.ImageUploadScreen -> {
                    navigator?.navigateTo(AppScreen.ImageUpload(it.userId, it.userType))
                }
                is TeacherProfileNavigationState.ChangePasswordScreen -> {
                    navigator?.navigateTo(AppScreen.ChangePassword)
                }
                is TeacherProfileNavigationState.MyDeviceListScreen -> {
                    navigator?.navigateTo(AppScreen.MyDeviceList(it.userId, it.userType))
                }
                is TeacherProfileNavigationState.TeacherProfileEditScreen -> {
                    navigator?.navigateTo(
                        AppScreen.TeacherProfileEdit(it.userId, it.action),
                    )
                }
                is TeacherProfileNavigationState.DeleteAccountScreen -> {
                    navigator?.navigateTo(AppScreen.DeleteAccount(it.userId, it.userType))
                }
            }
            onNavigationHandled()
        }
    }
}
