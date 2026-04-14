package com.workfort.pstuian.ui.studentprofile.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.common.composable.ProfileInfoListView
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowInputDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.composable.TabView
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.featuredomain.model.ProfileInfoItem
import com.workfort.pstuian.featuredomain.model.ProfileInfoItemAction
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.studentprofile.state.MessageState
import com.workfort.pstuian.ui.studentprofile.state.ProfileState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiEvent
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_bio
import pstuian.feature_presentation.generated.resources.hint_upload_new_cv
import pstuian.feature_presentation.generated.resources.msg_sign_out
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_account
import pstuian.feature_presentation.generated.resources.txt_address
import pstuian.feature_presentation.generated.resources.txt_batch
import pstuian.feature_presentation.generated.resources.txt_blood_donation
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_bio
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_cv
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_facebook
import pstuian.feature_presentation.generated.resources.txt_faculty
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_linked_in
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_msg_email
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list
import pstuian.feature_presentation.generated.resources.txt_my_donation_list
import pstuian.feature_presentation.generated.resources.txt_name
import pstuian.feature_presentation.generated.resources.txt_option
import pstuian.feature_presentation.generated.resources.txt_password
import pstuian.feature_presentation.generated.resources.txt_phone
import pstuian.feature_presentation.generated.resources.txt_registration_number
import pstuian.feature_presentation.generated.resources.txt_session
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_signed_in_devices
import pstuian.feature_presentation.generated.resources.txt_title_call
import pstuian.feature_presentation.generated.resources.txt_title_email
import pstuian.feature_presentation.generated.resources.txt_update

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileContentPanel(
    uiState: StudentProfileUiState,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    Scaffold(
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
                                StudentProfileUiEvent.ClickEdit(
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
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (val state = uiState.profileState) {
                is ProfileState.None -> Unit
                is ProfileState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ProfileState.Available -> {
                    ProfileView(state.profile, uiState.selectedTabIndex, onUiEvent)
                }
                is ProfileState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        AnimatedErrorView(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }

    uiState.messageState?.let {
        HandleMessageState(it, onUiEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfileView(
    profile: StudentProfile,
    selectedTabIndex: Int,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabs = getStudentsTabs(profile.isSignedIn)
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    LaunchedEffect(key1 = pagerState.currentPage) {
        onUiEvent(StudentProfileUiEvent.ClickTab(pagerState.currentPage))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { onUiEvent(StudentProfileUiEvent.ClickBack) },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.student.imageUrl
                val imageModifier = if (imageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(StudentProfileUiEvent.ClickImage(imageUrl))
                    }
                }
                LoadAsyncUserImage(
                    modifier = imageModifier,
                    url = imageUrl,
                    size = 96.dp,
                )
                if (profile.isSignedIn) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .clickable {
                                onUiEvent(StudentProfileUiEvent.ClickChangeImage)
                            },
                    )
                }
            }
            IconButton(
                onClick = {
                    onUiEvent(
                        if (profile.isSignedIn) {
                            StudentProfileUiEvent.ClickSignOut
                        } else {
                            StudentProfileUiEvent.ClickCall
                        }
                    )
                },
            ) {
                if (profile.isSignedIn) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Action button",
                    )
                } else {
                    Icon(
                        Icons.Filled.Call,
                        contentDescription = "Action button",
                    )
                }
            }
        }
        TitleTextSmall(
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            text = profile.student.name,
        )
        profile.student.bio?.let { bio ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = bio,
                textAlign = TextAlign.Center,
            )
        }
        if (profile.isSignedIn) {
            Text(
                text = "Edit Bio",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onUiEvent(StudentProfileUiEvent.ClickEditBio)
                    }
                    .padding(horizontal = 8.dp),
            )
        }
        TabView(
            tabs = getStudentsTabs(profile.isSignedIn),
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getStudentAcademicTabItems(profile).ProfileInfoListView {
                    HandleProfileInfoItemAction(it.action, onUiEvent)
                }
                1 -> getStudentConnectTabItems(profile).ProfileInfoListView {
                    HandleProfileInfoItemAction(it.action, onUiEvent)
                }
                2 -> if (profile.isSignedIn) {
                    getStudentOptionTabItems().ProfileInfoListView {
                        HandleProfileInfoItemAction(it.action, onUiEvent)
                    }
                }
            }
        }
    }
}

private fun HandleProfileInfoItemAction(
    action: ProfileInfoItemAction,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    when (action) {
        is ProfileInfoItemAction.None -> Unit
        is ProfileInfoItemAction.Edit -> Unit
        is ProfileInfoItemAction.Call -> onUiEvent(StudentProfileUiEvent.ClickCall)
        is ProfileInfoItemAction.Email -> onUiEvent(StudentProfileUiEvent.ClickEmail)
        is ProfileInfoItemAction.DownloadCv -> onUiEvent(StudentProfileUiEvent.ClickDownloadCv(action.url))
        is ProfileInfoItemAction.Link -> onUiEvent(StudentProfileUiEvent.ClickImage(action.url)) // Or handle link separately if needed
        is ProfileInfoItemAction.Password -> onUiEvent(StudentProfileUiEvent.ClickChangePassword)
        is ProfileInfoItemAction.UploadCv -> onUiEvent(StudentProfileUiEvent.ClickUploadCv)
        is ProfileInfoItemAction.BloodDonationList -> onUiEvent(StudentProfileUiEvent.ClickMyBloodDonationList)
        is ProfileInfoItemAction.CheckInList -> onUiEvent(StudentProfileUiEvent.ClickMyCheckInList)
        is ProfileInfoItemAction.SignedInDevices -> onUiEvent(StudentProfileUiEvent.ClickMyDeviceList)
        is ProfileInfoItemAction.DeleteAccount -> onUiEvent(StudentProfileUiEvent.ClickDeleteAccount)
    }
}

@Composable
private fun getStudentsTabs(isSignedIn: Boolean) = arrayListOf(
    stringResource(Res.string.txt_academic),
    stringResource(Res.string.txt_connect),
).also {
    if (isSignedIn) {
        it.add(stringResource(Res.string.txt_option))
    }
}

@Composable
private fun getStudentAcademicTabItems(profile: StudentProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.student.name),
    ProfileInfoItem(stringResource(Res.string.txt_id), profile.student.id.toString()),
    ProfileInfoItem(stringResource(Res.string.txt_registration_number), profile.student.reg),
    ProfileInfoItem(stringResource(Res.string.txt_blood_group), profile.student.blood ?: "~"),
    ProfileInfoItem(stringResource(Res.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(stringResource(Res.string.txt_batch), profile.batch.name),
    ProfileInfoItem(stringResource(Res.string.txt_session), profile.student.session),
)

@Composable
private fun getStudentConnectTabItems(profile: StudentProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_address), profile.student.address ?: "~"),
    ProfileInfoItem(
        stringResource(Res.string.txt_phone),
        profile.student.phone ?: "~",
        if (profile.student.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.student.phone.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_email),
        profile.student.email ?: "~",
        if (profile.student.email.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Email(profile.student.email.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_cv),
        profile.student.cvLink ?: "~",
        if (profile.student.cvLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.DownloadCv(profile.student.cvLink.orEmpty())
        }
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_linked_in),
        profile.student.linkedIn ?: "~",
        if (profile.student.linkedIn.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.student.linkedIn.orEmpty())
        }
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_facebook),
        profile.student.fbLink ?: "~",
        if (profile.student.fbLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.student.fbLink.orEmpty())
        },
    ),
)

@Composable
private fun getStudentOptionTabItems() = listOf(
    ProfileInfoItem(
        stringResource(Res.string.txt_password),
        stringResource(Res.string.txt_change_password),
        ProfileInfoItemAction.Password,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_cv),
        stringResource(Res.string.hint_upload_new_cv),
        ProfileInfoItemAction.UploadCv,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_blood_donation),
        stringResource(Res.string.txt_my_donation_list),
        ProfileInfoItemAction.BloodDonationList,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_check_in),
        stringResource(Res.string.txt_my_check_in_list),
        ProfileInfoItemAction.CheckInList,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_devices),
        stringResource(Res.string.txt_signed_in_devices),
        ProfileInfoItemAction.SignedInDevices,
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_account),
        stringResource(Res.string.txt_delete_account),
        ProfileInfoItemAction.DeleteAccount,
    ),
)

@Composable
private fun HandleMessageState(
    messageState: MessageState,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    when (messageState) {
        is MessageState.Loading -> {
            ShowLoaderDialog(cancelable = messageState.cancelable)
        }
        is MessageState.InputBio -> {
            ShowInputDialog(
                title = stringResource(Res.string.txt_change_bio),
                label = stringResource(Res.string.hint_bio),
                input = messageState.currentBio,
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                maxLength = 150,
                confirmButtonText = stringResource(Res.string.txt_update),
                onConfirm = {
                    onUiEvent(StudentProfileUiEvent.ChangeBio(it))
                },
                onDismiss = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                }
            )
        }
        is MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" ${messageState.phoneNumber}"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                    onUiEvent(StudentProfileUiEvent.OnCall(messageState.phoneNumber))
                },
                onDismiss = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                }
            )
        }
        is MessageState.Email -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Email,
                title = stringResource(Res.string.txt_title_email),
                message = stringResource(Res.string.txt_msg_email).plus(" ${messageState.email}"),
                confirmButtonText = stringResource(Res.string.txt_email),
                onConfirm = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                    onUiEvent(StudentProfileUiEvent.OnEmail(messageState.email))
                },
                onDismiss = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                }
            )
        }
        is MessageState.ConfirmSignOut -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_sign_out),
                message = stringResource(Res.string.msg_sign_out),
                onConfirm = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                    onUiEvent(StudentProfileUiEvent.SignOut)
                },
                onDismiss = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                },
            )
        }
        is MessageState.Success -> {
            ShowSuccessDialog(
                message = messageState.message,
                onConfirm = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                },
            )
        }
        is MessageState.Error -> {
            ShowErrorDialog(
                message = messageState.message,
                onConfirm = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(StudentProfileUiEvent.MessageConsumed)
                },
            )
        }
    }
}
