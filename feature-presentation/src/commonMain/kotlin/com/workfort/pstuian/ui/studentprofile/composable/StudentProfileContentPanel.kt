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
import androidx.compose.material.icons.filled.PhotoCamera
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
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.LoadAsyncUserImage
import com.workfort.pstuian.common.composable.LoadingOverlay
import com.workfort.pstuian.common.composable.ProfileInfoListView
import com.workfort.pstuian.common.composable.TabView
import com.workfort.pstuian.common.composable.TitleTextSmall
import com.workfort.pstuian.featuredomain.model.ProfileInfoItem
import com.workfort.pstuian.featuredomain.model.ProfileInfoItemAction
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.studentprofile.state.ProfileState
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiEvent
import com.workfort.pstuian.ui.studentprofile.state.StudentProfileUiState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_upload_new_cv
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_account
import pstuian.feature_presentation.generated.resources.txt_address
import pstuian.feature_presentation.generated.resources.txt_batch
import pstuian.feature_presentation.generated.resources.txt_blood_donation
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_cv
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_devices
import pstuian.feature_presentation.generated.resources.txt_edit_bio
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_facebook
import pstuian.feature_presentation.generated.resources.txt_faculty
import pstuian.feature_presentation.generated.resources.txt_go_back
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_linked_in
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

@Composable
fun StudentProfileContentPanel(
    uiState: StudentProfileUiState,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    when (val state = uiState.profileState) {
        is ProfileState.None -> Unit
        is ProfileState.Loading -> LoadingOverlay()
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
        onUiEvent(StudentProfileUiEvent.TabClicked(pagerState.currentPage))
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
                onClick = { onUiEvent(StudentProfileUiEvent.BackClicked) },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.txt_go_back),
                )
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                val imageUrl = profile.student.imageUrl
                val imageModifier = if (imageUrl.isNullOrEmpty()) {
                    Modifier
                } else {
                    Modifier.clickable {
                        onUiEvent(StudentProfileUiEvent.ImageClicked(imageUrl))
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
                                onUiEvent(StudentProfileUiEvent.ChangeImageClicked)
                            },
                    )
                }
            }
            IconButton(
                onClick = {
                    onUiEvent(
                        if (profile.isSignedIn) {
                            StudentProfileUiEvent.SignOutClicked
                        } else {
                            StudentProfileUiEvent.CallClicked
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
                text = stringResource(Res.string.txt_edit_bio),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onUiEvent(StudentProfileUiEvent.EditBioClicked)
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
        is ProfileInfoItemAction.Call -> onUiEvent(StudentProfileUiEvent.CallClicked)
        is ProfileInfoItemAction.Email -> onUiEvent(StudentProfileUiEvent.EmailClicked)
        is ProfileInfoItemAction.DownloadCv -> onUiEvent(StudentProfileUiEvent.DownloadCvClicked(action.url))
        is ProfileInfoItemAction.Link -> onUiEvent(StudentProfileUiEvent.ImageClicked(action.url)) // Or handle link separately if needed
        is ProfileInfoItemAction.Password -> onUiEvent(StudentProfileUiEvent.ChangePasswordClicked)
        is ProfileInfoItemAction.UploadCv -> onUiEvent(StudentProfileUiEvent.UploadCvClicked)
        is ProfileInfoItemAction.BloodDonationList -> onUiEvent(StudentProfileUiEvent.MyBloodDonationListClicked)
        is ProfileInfoItemAction.CheckInList -> onUiEvent(StudentProfileUiEvent.MyCheckInListClicked)
        is ProfileInfoItemAction.SignedInDevices -> onUiEvent(StudentProfileUiEvent.MyDeviceListClicked)
        is ProfileInfoItemAction.DeleteAccount -> onUiEvent(StudentProfileUiEvent.DeleteAccountClicked)
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
