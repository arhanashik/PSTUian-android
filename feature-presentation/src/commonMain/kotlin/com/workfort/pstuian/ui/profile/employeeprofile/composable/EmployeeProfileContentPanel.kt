package com.workfort.pstuian.ui.profile.employeeprofile.composable

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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.ProfileInfoItem
import com.workfort.pstuian.featuredomain.model.ProfileInfoItemAction
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.ProfileInfoListView
import com.workfort.pstuian.ui.common.composable.TabView
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.profile.employeeprofile.state.EmployeeProfileUiEvent
import com.workfort.pstuian.ui.profile.employeeprofile.state.EmployeeProfileUiState
import com.workfort.pstuian.ui.profile.employeeprofile.state.ProfileState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_account
import pstuian.feature_presentation.generated.resources.txt_address
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_department
import pstuian.feature_presentation.generated.resources.txt_designation
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_edit_bio
import pstuian.feature_presentation.generated.resources.txt_faculty
import pstuian.feature_presentation.generated.resources.txt_go_back
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_name
import pstuian.feature_presentation.generated.resources.txt_option
import pstuian.feature_presentation.generated.resources.txt_password
import pstuian.feature_presentation.generated.resources.txt_phone
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_signed_in_devices

@Composable
fun EmployeeProfileContentPanel(
    uiState: EmployeeProfileUiState,
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    Column {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfileView(
    profile: EmployeeProfile,
    selectedTabIndex: Int,
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabs = getEmployeeTabs(profile.isSignedIn)
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    LaunchedEffect(key1 = pagerState.currentPage) {
        onUiEvent(EmployeeProfileUiEvent.TabClicked(pagerState.currentPage))
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
                onClick = { onUiEvent(EmployeeProfileUiEvent.BackClicked) },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.txt_go_back),
                )
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.employee.imageUrl
                val imageModifier = if (imageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(EmployeeProfileUiEvent.ImageClicked(imageUrl))
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
                                onUiEvent(EmployeeProfileUiEvent.ChangeImageClicked)
                            },
                    )
                }
            }
            IconButton(
                onClick = {
                    onUiEvent(
                        if (profile.isSignedIn) {
                            EmployeeProfileUiEvent.SignOutClicked
                        } else {
                            EmployeeProfileUiEvent.CallClicked
                        }
                    )
                },
            ) {
                if (profile.isSignedIn) {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = stringResource(Res.string.txt_sign_out),
                    )
                } else {
                    Icon(
                        Icons.Filled.Call,
                        contentDescription = stringResource(Res.string.txt_call),
                    )
                }
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
        if (profile.isSignedIn) {
            Text(
                text = stringResource(Res.string.txt_edit_bio),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onUiEvent(EmployeeProfileUiEvent.EditBioClicked)
                    }
                    .padding(horizontal = 8.dp),
            )
        }
        TabView(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getEmployeeAcademicTabItems(profile).ProfileInfoListView {
                    it.action.handleProfileInfoItemAction(onUiEvent)
                }
                1 -> getEmployeeConnectTabItems(profile).ProfileInfoListView {
                    it.action.handleProfileInfoItemAction(onUiEvent)
                }
                2 -> if (profile.isSignedIn) {
                    getEmployeeOptionTabItems().ProfileInfoListView {
                        it.action.handleProfileInfoItemAction(onUiEvent)
                    }
                }
            }
        }
    }
}

private fun ProfileInfoItemAction.handleProfileInfoItemAction(
    onUiEvent: (EmployeeProfileUiEvent) -> Unit,
) {
    when (this) {
        is ProfileInfoItemAction.Call -> onUiEvent(EmployeeProfileUiEvent.CallClicked)
        is ProfileInfoItemAction.Email -> onUiEvent(EmployeeProfileUiEvent.EmailClicked)
        is ProfileInfoItemAction.Password -> onUiEvent(EmployeeProfileUiEvent.ChangePasswordClicked)
        is ProfileInfoItemAction.SignedInDevices -> onUiEvent(EmployeeProfileUiEvent.MyDeviceListClicked)
        is ProfileInfoItemAction.DeleteAccount -> onUiEvent(EmployeeProfileUiEvent.DeleteAccountClicked)
        else -> Unit
    }
}

@Composable
private fun getEmployeeTabs(isSignedIn: Boolean) = arrayListOf(
    stringResource(Res.string.txt_academic),
    stringResource(Res.string.txt_connect),
).also {
    if (isSignedIn) {
        it.add(stringResource(Res.string.txt_option))
    }
}

@Composable
private fun getEmployeeOptionTabItems() = listOf(
    ProfileInfoItem(
        stringResource(Res.string.txt_password),
        stringResource(Res.string.txt_change_password),
        ProfileInfoItemAction.Password,
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
private fun getEmployeeAcademicTabItems(profile: EmployeeProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.employee.name),
    ProfileInfoItem(stringResource(Res.string.txt_id), profile.employee.userId),
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
