package com.workfort.pstuian.ui.employeeprofile.composable

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.common.composable.ProfileInfoListView
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.composable.TabView
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.ProfileInfoItem
import com.workfort.pstuian.featuredomain.model.ProfileInfoItemAction
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileUiEvent
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileUiState
import com.workfort.pstuian.ui.employeeprofile.state.MessageState
import com.workfort.pstuian.ui.employeeprofile.state.ProfileState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_address
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_designation
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_department
import pstuian.feature_presentation.generated.resources.txt_faculty
import pstuian.feature_presentation.generated.resources.txt_name
import pstuian.feature_presentation.generated.resources.txt_phone
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
fun EmployeeProfileContentPanel(
    uiState: EmployeeProfileUiState,
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    Scaffold { innerPadding ->
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
    profile: EmployeeProfile,
    selectedTabIndex: Int,
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabs = getEmployeeTabs()
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    LaunchedEffect(key1 = pagerState.currentPage) {
        onUiEvent(EmployeeProfileUiEvent.ClickTab(pagerState.currentPage))
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
                onClick = { onUiEvent(EmployeeProfileUiEvent.ClickBack) },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.employee.imageUrl
                val imageModifier = if (imageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(EmployeeProfileUiEvent.ClickImage(imageUrl))
                    }
                }
                LoadAsyncUserImage(
                    modifier = imageModifier,
                    url = imageUrl,
                    size = 96.dp,
                )
            }
            IconButton(
                onClick = { onUiEvent(EmployeeProfileUiEvent.ClickCall) },
            ) {
                Icon(Icons.Filled.Call, contentDescription = "Action button")
            }
        }
        TitleTextSmall(
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            text = profile.employee.name,
        )
        profile.employee.bio?.let { bio ->
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = bio,
                textAlign = TextAlign.Center,
            )
        }
        TabView(
            tabs = getEmployeeTabs(),
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getEmployeeAcademicTabItems(profile).ProfileInfoListView {
                    HandleProfileInfoItemAction(it.action, onUiEvent)
                }
                1 -> getEmployeeConnectTabItems(profile).ProfileInfoListView {
                    HandleProfileInfoItemAction(it.action, onUiEvent)
                }
            }
        }
    }
}

private fun HandleProfileInfoItemAction(
    action: ProfileInfoItemAction,
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    when (action) {
        is ProfileInfoItemAction.Call -> onUiEvent(EmployeeProfileUiEvent.ClickCall)
        else -> Unit
    }
}

@Composable
private fun getEmployeeTabs() = listOf(
    stringResource(Res.string.txt_academic),
    stringResource(Res.string.txt_connect),
)

@Composable
private fun getEmployeeAcademicTabItems(profile: EmployeeProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.employee.name),
    ProfileInfoItem(stringResource(Res.string.txt_id), profile.employee.id.toString()),
    ProfileInfoItem(stringResource(Res.string.txt_designation), profile.employee.designation),
    ProfileInfoItem(stringResource(Res.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(stringResource(Res.string.txt_department), profile.employee.department ?: "~"),
    ProfileInfoItem(stringResource(Res.string.txt_blood_group), profile.employee.blood ?: "~"),
)

@Composable
private fun getEmployeeConnectTabItems(profile: EmployeeProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_address), profile.employee.address ?: "~"),
    ProfileInfoItem(
        stringResource(Res.string.txt_phone),
        profile.employee.phone ?: "~",
        if (profile.employee.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.employee.phone.orEmpty())
        },
    ),
)

@Composable
private fun HandleMessageState(
    messageState: MessageState,
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    when (messageState) {
        is MessageState.Loading -> {
            ShowLoaderDialog(cancelable = messageState.cancelable)
        }
        is MessageState.Call -> {
            ShowConfirmationDialog(
                icon = Icons.Default.Call,
                title = stringResource(Res.string.txt_title_call),
                message = stringResource(Res.string.txt_msg_call).plus(" ${messageState.phoneNumber}"),
                confirmButtonText = stringResource(Res.string.txt_call),
                onConfirm = {
                    onUiEvent(EmployeeProfileUiEvent.MessageConsumed)
                    onUiEvent(EmployeeProfileUiEvent.OnCall(messageState.phoneNumber))
                },
                onDismiss = {
                    onUiEvent(EmployeeProfileUiEvent.MessageConsumed)
                }
            )
        }
        is MessageState.Success -> {
            ShowSuccessDialog(
                message = messageState.message,
                onConfirm = {
                    onUiEvent(EmployeeProfileUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(EmployeeProfileUiEvent.MessageConsumed)
                },
            )
        }
        is MessageState.Error -> {
            ShowErrorDialog(
                message = messageState.message,
                onConfirm = {
                    onUiEvent(EmployeeProfileUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(EmployeeProfileUiEvent.MessageConsumed)
                },
            )
        }
        else -> Unit
    }
}
