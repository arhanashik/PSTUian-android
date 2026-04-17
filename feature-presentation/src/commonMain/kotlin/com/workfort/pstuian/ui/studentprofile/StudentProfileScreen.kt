package com.workfort.pstuian.ui.studentprofile

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
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowInputDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.studentprofile.composable.StudentProfileContentPanel
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileMessageState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileNavigationState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiEvent
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiState
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
fun StudentProfileScreen(viewModel: StudentProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    StudentProfileScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::messageHandled)
    HandleNavigationState(navigation, viewModel::navigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentProfileScreenContent(
    uiState: StudentProfileUiState,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold (
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
                                StudentProfileUiEvent.EditClicked(
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
        StudentProfileContentPanel(uiState, onUiEvent)
    }
}

@Composable
private fun HandleMessageState(
    message: StudentProfileMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (message) {
            is StudentProfileMessageState.Loading -> {
                ShowLoaderDialog(cancelable = message.cancelable)
            }
            is StudentProfileMessageState.InputBio -> {
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
            is StudentProfileMessageState.CallConfirmation -> {
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
            is StudentProfileMessageState.EmailConfirmation -> {
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
            is StudentProfileMessageState.ConfirmSignOut -> {
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
            is StudentProfileMessageState.Success -> {
                ShowSuccessDialog(
                    message = message.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.Error -> {
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
    navigation: StudentProfileNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is StudentProfileNavigationState.GoBack -> navigator?.toString()
                is StudentProfileNavigationState.ImagePreviewScreen -> {
                    navigator?.navigateTo(AppScreen.ImagePreview(it.encodedImageUrl))
                }
                is StudentProfileNavigationState.ImageUploadScreen -> {
                    navigator?.navigateTo(AppScreen.ImageUpload(it.userId, it.userType))
                }
                is StudentProfileNavigationState.ChangePasswordScreen -> {
                    navigator?.navigateTo(AppScreen.ChangePassword)
                }
                is StudentProfileNavigationState.DownloadCvScreen -> {
                    navigator?.navigateTo(
                        AppScreen.DownloadCv(it.userId, it.userType, it.url),
                    )
                }
                is StudentProfileNavigationState.UploadCvScreen -> {
                    navigator?.navigateTo(AppScreen.UploadCv(it.userId, it.userType))
                }
                is StudentProfileNavigationState.MyBloodDonationListScreen -> {
                    navigator?.navigateTo(
                        AppScreen.MyBloodDonationList(it.userId, it.userType),
                    )
                }
                is StudentProfileNavigationState.MyCheckInListScreen -> {
                    navigator?.navigateTo(AppScreen.MyCheckInList(it.userId, it.userType))
                }
                is StudentProfileNavigationState.MyDeviceListScreen -> {
                    navigator?.navigateTo(AppScreen.MyDeviceList(it.userId, it.userType))
                }
                is StudentProfileNavigationState.StudentProfileEditScreen -> {
                    navigator?.navigateTo(
                        AppScreen.StudentProfileEdit(it.userId, it.action),
                    )
                }
                is StudentProfileNavigationState.DeleteAccountScreen -> {
                    navigator?.navigateTo(AppScreen.DeleteAccount(it.userId, it.userType))
                }
            }
            onNavigationHandled()
        }
    }
}
