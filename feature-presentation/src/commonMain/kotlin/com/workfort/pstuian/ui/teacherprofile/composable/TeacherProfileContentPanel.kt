package com.workfort.pstuian.ui.teacherprofile.composable

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
import com.workfort.pstuian.featuredomain.model.ProfileInfoItem
import com.workfort.pstuian.featuredomain.model.ProfileInfoItemAction
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.ui.common.composable.ProfileInfoListView
import com.workfort.pstuian.ui.common.composable.TabView
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.teacherprofile.state.ProfileState
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiEvent
import com.workfort.pstuian.ui.teacherprofile.state.TeacherProfileUiState
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
import pstuian.feature_presentation.generated.resources.txt_description
import pstuian.feature_presentation.generated.resources.txt_designation
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_edit_bio
import pstuian.feature_presentation.generated.resources.txt_email
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
fun TeacherProfileContentPanel(
    uiState: TeacherProfileUiState,
    onUiEvent: (TeacherProfileUiEvent) -> Unit,
) {
    Column(modifier = Modifier) {
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
    profile: TeacherProfile,
    selectedTabIndex: Int,
    onUiEvent: (TeacherProfileUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tabs = getTeacherTabs(profile.isSignedIn)
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    LaunchedEffect(key1 = pagerState.currentPage) {
        onUiEvent(TeacherProfileUiEvent.TabClicked(pagerState.currentPage))
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
                onClick = { onUiEvent(TeacherProfileUiEvent.BackClicked) },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.txt_go_back),
                )
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.teacher.imageUrl
                val imageModifier = if (imageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(TeacherProfileUiEvent.ImageClicked(imageUrl))
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
                                onUiEvent(TeacherProfileUiEvent.ChangeImageClicked)
                            },
                    )
                }
            }
            IconButton(
                onClick = {
                    onUiEvent(
                        if (profile.isSignedIn) {
                            TeacherProfileUiEvent.SignOutClicked
                        } else {
                            TeacherProfileUiEvent.CallClicked
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
            text = profile.teacher.name,
        )
        profile.teacher.bio?.let { bio ->
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
                        onUiEvent(TeacherProfileUiEvent.EditBioClicked)
                    }
                    .padding(horizontal = 8.dp),
            )
        }
        TabView(
            tabs = getTeacherTabs(profile.isSignedIn),
            selectedTabIndex = selectedTabIndex,
        ) { index ->
            scope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> getTeacherAcademicTabItems(profile).ProfileInfoListView {
                    HandleProfileInfoItemAction(it.action, onUiEvent)
                }
                1 -> getTeacherConnectTabItems(profile).ProfileInfoListView {
                    HandleProfileInfoItemAction(it.action, onUiEvent)
                }
                2 -> if (profile.isSignedIn) {
                    getTeacherOptionTabItems().ProfileInfoListView {
                        HandleProfileInfoItemAction(it.action, onUiEvent)
                    }
                }
            }
        }
    }
}

private fun HandleProfileInfoItemAction(
    action: ProfileInfoItemAction,
    onUiEvent: (TeacherProfileUiEvent) -> Unit,
) {
    when (action) {
        is ProfileInfoItemAction.None -> Unit
        is ProfileInfoItemAction.Edit -> Unit
        is ProfileInfoItemAction.Call -> onUiEvent(TeacherProfileUiEvent.CallClicked)
        is ProfileInfoItemAction.Email -> onUiEvent(TeacherProfileUiEvent.EmailClicked)
        is ProfileInfoItemAction.Password -> onUiEvent(TeacherProfileUiEvent.ChangePasswordClicked)
        is ProfileInfoItemAction.SignedInDevices -> onUiEvent(TeacherProfileUiEvent.MyDeviceListClicked)
        is ProfileInfoItemAction.DeleteAccount -> onUiEvent(TeacherProfileUiEvent.DeleteAccountClicked)
        else -> Unit
    }
}

@Composable
private fun getTeacherTabs(isSignedIn: Boolean) = arrayListOf(
    stringResource(Res.string.txt_academic),
    stringResource(Res.string.txt_connect),
).also {
    if (isSignedIn) {
        it.add(stringResource(Res.string.txt_option))
    }
}

@Composable
private fun getTeacherAcademicTabItems(profile: TeacherProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.teacher.name),
    ProfileInfoItem(stringResource(Res.string.txt_id), profile.teacher.id.toString()),
    ProfileInfoItem(stringResource(Res.string.txt_designation), profile.teacher.designation),
    ProfileInfoItem(stringResource(Res.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(stringResource(Res.string.txt_department), profile.teacher.department),
    ProfileInfoItem(stringResource(Res.string.txt_blood_group), profile.teacher.blood ?: "~"),
    ProfileInfoItem(stringResource(Res.string.txt_description), profile.teacher.description ?: "~"),
)

@Composable
private fun getTeacherConnectTabItems(profile: TeacherProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_address), profile.teacher.address ?: "~"),
    ProfileInfoItem(
        stringResource(Res.string.txt_phone),
        profile.teacher.phone ?: "~",
        if (profile.teacher.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.teacher.phone.orEmpty())
        },
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_email),
        profile.teacher.email ?: "~",
        if (profile.teacher.email.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Email(profile.teacher.email.orEmpty())
        },
    ),
)

@Composable
private fun getTeacherOptionTabItems() = listOf(
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
