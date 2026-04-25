package com.workfort.pstuian.ui.profile.studentprofile.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.ProfileInfoItem
import com.workfort.pstuian.featuredomain.model.ProfileInfoItemAction
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ProfileInfoListView
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.profile.studentprofile.state.ProfileState
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileUiEvent
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileUiState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_address
import pstuian.feature_presentation.generated.resources.txt_batch
import pstuian.feature_presentation.generated.resources.txt_blood_group
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_cv
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_facebook
import pstuian.feature_presentation.generated.resources.txt_faculty
import pstuian.feature_presentation.generated.resources.txt_id
import pstuian.feature_presentation.generated.resources.txt_linked_in
import pstuian.feature_presentation.generated.resources.txt_name
import pstuian.feature_presentation.generated.resources.txt_phone
import pstuian.feature_presentation.generated.resources.txt_registration_number
import pstuian.feature_presentation.generated.resources.txt_session

@Composable
fun StudentProfileContentPanel(
    uiState: StudentProfileUiState,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    when (val state = uiState.profileState) {
        is ProfileState.None -> Unit
        is ProfileState.Loading -> StudentProfileShimmer()
        is ProfileState.Available -> {
            ProfileView(
                profile = state.profile,
                isSignedIn = uiState.isSignedIn,
                selectedTabIndex = uiState.selectedTabIndex,
                onUiEvent = onUiEvent,
            )
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
    isSignedIn: Boolean,
    selectedTabIndex: Int,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val academicLabel = stringResource(Res.string.txt_academic)
    val connectLabel = stringResource(Res.string.txt_connect)

    LaunchedEffect(pagerState.currentPage) {
        onUiEvent(StudentProfileUiEvent.TabClicked(pagerState.currentPage))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        StudentProfileTopBar(
            isSignedIn = isSignedIn,
            profile = profile,
            onUiEvent = onUiEvent,
        )

        val headerCardGradient = Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                MaterialTheme.colorScheme.surface,
            ),
        )

        // Header card (no elevation)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(headerCardGradient),
        ) {
            StudentProfileHeader(
                profile = profile,
                isSignedIn = isSignedIn,
                selectedTabIndex = selectedTabIndex,
                onUiEvent = onUiEvent,
            )
        }

        // Toggle is outside content card, between header and content
        ToggleSwitch(
            options = listOf(academicLabel, connectLabel),
            selectedIndex = selectedTabIndex,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            onSelectedIndexChange = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            },
        )

        // Content card under the toggle switch
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) { page ->
                when (page) {
                    0 -> getAcademicTabItems(profile).ProfileInfoListView {
                        handleProfileInfoItemAction(it.action, onUiEvent)
                    }
                    1 -> getConnectTabItems(profile).ProfileInfoListView {
                        handleProfileInfoItemAction(it.action, onUiEvent)
                    }
                }
            }
        }
    }
}

// ── Tab item action dispatcher ────────────────────────────────────────────────

private fun handleProfileInfoItemAction(
    action: ProfileInfoItemAction,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    when (action) {
        is ProfileInfoItemAction.None -> Unit
        is ProfileInfoItemAction.Edit -> Unit
        is ProfileInfoItemAction.Call -> onUiEvent(StudentProfileUiEvent.CallClicked)
        is ProfileInfoItemAction.Email -> onUiEvent(StudentProfileUiEvent.EmailClicked)
        is ProfileInfoItemAction.DownloadCv -> onUiEvent(StudentProfileUiEvent.DownloadCvClicked(action.url))
        is ProfileInfoItemAction.Link -> onUiEvent(StudentProfileUiEvent.ImageClicked(action.url))
        is ProfileInfoItemAction.Password -> onUiEvent(StudentProfileUiEvent.ChangePasswordClicked)
        is ProfileInfoItemAction.UploadCv -> onUiEvent(StudentProfileUiEvent.UploadCvClicked)
        is ProfileInfoItemAction.BloodDonationList -> onUiEvent(StudentProfileUiEvent.MyBloodDonationListClicked)
        is ProfileInfoItemAction.CheckInList -> onUiEvent(StudentProfileUiEvent.MyCheckInListClicked)
        is ProfileInfoItemAction.SignedInDevices -> onUiEvent(StudentProfileUiEvent.MyDeviceListClicked)
        is ProfileInfoItemAction.DeleteAccount -> onUiEvent(StudentProfileUiEvent.DeleteAccountClicked)
    }
}

// ── Tab content builders ──────────────────────────────────────────────────────

@Composable
private fun getAcademicTabItems(profile: StudentProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_name), profile.student.name),
    ProfileInfoItem(stringResource(Res.string.txt_id), profile.student.userId),
    ProfileInfoItem(stringResource(Res.string.txt_registration_number), profile.student.reg),
    ProfileInfoItem(stringResource(Res.string.txt_blood_group), profile.student.blood ?: "~"),
    ProfileInfoItem(stringResource(Res.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(stringResource(Res.string.txt_batch), profile.batch.name),
    ProfileInfoItem(stringResource(Res.string.txt_session), profile.student.session),
)

@Composable
private fun getConnectTabItems(profile: StudentProfile) = listOf(
    ProfileInfoItem(stringResource(Res.string.txt_address), profile.student.address ?: "~"),
    ProfileInfoItem(
        stringResource(Res.string.txt_phone),
        profile.student.phone ?: "~",
        if (profile.student.phone.isNullOrEmpty()) ProfileInfoItemAction.None
        else ProfileInfoItemAction.Call(profile.student.phone.orEmpty()),
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_email),
        profile.student.email,
        if (profile.student.email.isEmpty()) ProfileInfoItemAction.None
        else ProfileInfoItemAction.Email(profile.student.email),
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_cv),
        profile.student.cvLink ?: "~",
        if (profile.student.cvLink.isNullOrEmpty()) ProfileInfoItemAction.None
        else ProfileInfoItemAction.DownloadCv(profile.student.cvLink.orEmpty()),
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_linked_in),
        profile.student.linkedIn ?: "~",
        if (profile.student.linkedIn.isNullOrEmpty()) ProfileInfoItemAction.None
        else ProfileInfoItemAction.Link(profile.student.linkedIn.orEmpty()),
    ),
    ProfileInfoItem(
        stringResource(Res.string.txt_facebook),
        profile.student.fbLink ?: "~",
        if (profile.student.fbLink.isNullOrEmpty()) ProfileInfoItemAction.None
        else ProfileInfoItemAction.Link(profile.student.fbLink.orEmpty()),
    ),
)

// ── Preview helpers ───────────────────────────────────────────────────────────

private fun mockProfile(withBio: Boolean = true) = StudentProfile(
    student = User.Student(
        userId = "42",
        studentId = 42,
        name = "Diana Richards",
        email = "diana.richards@pstu.ac.bd",
        facultyId = 1,
        phone = "+880 1711-000000",
        address = "Patuakhali, Bangladesh",
        bio = if (withBio) "Passionate about technology and innovation. CSE graduate." else null,
        blood = "B+",
        imageUrl = null,
        reg = "2018-215-001",
        batchId = 10,
        session = "2018-19",
        linkedIn = "https://linkedin.com/in/diana",
        fbLink = "https://facebook.com/diana",
        cvLink = null,
    ),
    faculty = Faculty(id = 1, shortTitle = "CSE", title = "Computer Science & Engineering", icon = null),
    batch = Batch(id = 10, name = "10th Batch", title = "Batch 10", session = "2018-19", facultyId = 1, totalStudent = 120, registeredStudent = 98),
    isSignedIn = false,
)

private fun mockUiState(
    isSignedIn: Boolean = false,
    withBio: Boolean = true,
    selectedTab: Int = 0,
) = StudentProfileUiState(
    selectedTabIndex = selectedTab,
    isSignedIn = isSignedIn,
    profileState = ProfileState.Available(mockProfile(withBio)),
)

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Light – Not Signed In")
@Composable
fun StudentProfileContentPanelPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Dark – Not Signed In")
@Composable
fun StudentProfileContentPanelDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        StudentProfileContentPanel(uiState = mockUiState(), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Signed In – Academic Tab")
@Composable
fun StudentProfileContentPanelSignedInPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(isSignedIn = true), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Signed In – Connect Tab")
@Composable
fun StudentProfileContentPanelConnectTabPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(isSignedIn = true, selectedTab = 1), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "No Bio")
@Composable
fun StudentProfileContentPanelNoBioPreview() {
    AppTheme {
        StudentProfileContentPanel(uiState = mockUiState(isSignedIn = true, withBio = false), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Shimmer Loading")
@Composable
fun StudentProfileContentPanelLoadingPreview() {
    AppTheme {
        StudentProfileContentPanel(
            uiState = StudentProfileUiState(profileState = ProfileState.Loading),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun StudentProfileContentPanelErrorPreview() {
    AppTheme {
        StudentProfileContentPanel(
            uiState = StudentProfileUiState(profileState = ProfileState.Error("Failed to load profile")),
            onUiEvent = {},
        )
    }
}
