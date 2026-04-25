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
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.profile.common.composable.ProfileHeader
import com.workfort.pstuian.ui.profile.common.composable.ProfileInfoListView
import com.workfort.pstuian.ui.profile.common.composable.ProfileTopBar
import com.workfort.pstuian.ui.profile.common.composable.StudentProfileShimmer
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_connect

@Composable
fun StudentProfileContentPanel(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    when (uiState) {
        is ProfileUiState.None -> Unit
        is ProfileUiState.Loading -> StudentProfileShimmer()
        is ProfileUiState.Content -> {
            ProfileView(
                headerDisplayData = uiState.headerDisplayData,
                academicContents = uiState.academicContents,
                connectContents = uiState.connectContents,
                isSignedIn = uiState.isSignedIn,
                selectedTabIndex = uiState.selectedTabIndex,
                onUiEvent = onUiEvent,
            )
        }
        is ProfileUiState.Error -> {
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
    headerDisplayData: ProfileHeaderDisplayData,
    academicContents: List<ProfileInfoItem>,
    connectContents: List<ProfileInfoItem>,
    isSignedIn: Boolean,
    selectedTabIndex: Int,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val academicLabel = stringResource(Res.string.txt_academic)
    val connectLabel = stringResource(Res.string.txt_connect)

    LaunchedEffect(pagerState.currentPage) {
        onUiEvent(ProfileUiEvent.TabClicked(pagerState.currentPage))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileTopBar(onNavigationBack = { onUiEvent(ProfileUiEvent.BackClicked) }) { expanded, onDismiss ->
            StudentProfileOptionsDropdown(
                expanded = expanded,
                isSignedIn = isSignedIn,
                onDismiss = onDismiss,
                onUiEvent = onUiEvent,
            )
        }

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
            ProfileHeader(
                displayData = headerDisplayData,
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
                    0 -> academicContents.ProfileInfoListView {
                        handleProfileInfoItemAction(it.action, onUiEvent)
                    }
                    1 -> connectContents.ProfileInfoListView {
                        handleProfileInfoItemAction(it.action, onUiEvent)
                    }
                }
            }
        }
    }
}

private fun handleProfileInfoItemAction(
    action: ProfileInfoItemAction,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    when (action) {
        is ProfileInfoItemAction.None -> Unit
        is ProfileInfoItemAction.Edit -> Unit
        is ProfileInfoItemAction.Call -> onUiEvent(ProfileUiEvent.CallClicked)
        is ProfileInfoItemAction.Email -> onUiEvent(ProfileUiEvent.EmailClicked)
        is ProfileInfoItemAction.DownloadCv -> onUiEvent(ProfileUiEvent.DownloadCvClicked(action.url))
        is ProfileInfoItemAction.Link -> onUiEvent(ProfileUiEvent.ImageClicked(action.url))
        is ProfileInfoItemAction.Password -> onUiEvent(ProfileUiEvent.ChangePasswordClicked)
        is ProfileInfoItemAction.UploadCv -> onUiEvent(ProfileUiEvent.UploadCvClicked)
        is ProfileInfoItemAction.BloodDonationList -> onUiEvent(ProfileUiEvent.MyBloodDonationListClicked)
        is ProfileInfoItemAction.CheckInList -> onUiEvent(ProfileUiEvent.MyCheckInListClicked)
        is ProfileInfoItemAction.SignedInDevices -> onUiEvent(ProfileUiEvent.MyDeviceListClicked)
        is ProfileInfoItemAction.DeleteAccount -> onUiEvent(ProfileUiEvent.DeleteAccountClicked)
    }
}
