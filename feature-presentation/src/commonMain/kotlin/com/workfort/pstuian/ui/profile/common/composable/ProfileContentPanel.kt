package com.workfort.pstuian.ui.profile.common.composable

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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.ApplySystemBarColors
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import com.workfort.pstuian.ui.profile.common.state.ProfileUiState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_connect

@Composable
fun ProfileContentPanel(
    uiState: ProfileUiState,
    onUiEvent: (ProfileUiEvent) -> Unit,
    optionsDropdown: @Composable (
        expanded: Boolean,
        onDismiss: () -> Unit,
        isSignedIn: Boolean,
        onUiEvent: (ProfileUiEvent) -> Unit,
    ) -> Unit,
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
                userPresenceDisplayData = uiState.userPresenceDisplayData,
                selectedTabIndex = uiState.selectedTabIndex,
                onUiEvent = onUiEvent,
                optionsDropdown = optionsDropdown,
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
    userPresenceDisplayData: UserPresenceDisplayData,
    selectedTabIndex: Int,
    onUiEvent: (ProfileUiEvent) -> Unit,
    optionsDropdown: @Composable (
        expanded: Boolean,
        onDismiss: () -> Unit,
        isSignedIn: Boolean,
        onUiEvent: (ProfileUiEvent) -> Unit,
    ) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabOptions = listOf(stringResource(Res.string.txt_academic), stringResource(Res.string.txt_connect))
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    LaunchedEffect(pagerState.currentPage) {
        onUiEvent(ProfileUiEvent.TabClicked(pagerState.currentPage))
    }

    ApplySystemBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        statusBarDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f,
        navigationBarColor = MaterialTheme.colorScheme.background,
        navigationBarDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f,
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileTopBar(
            title = "Profile",
            onNavigationBack = { onUiEvent(ProfileUiEvent.BackClicked) },
        ) { expanded, onDismiss ->
            optionsDropdown(expanded, onDismiss, isSignedIn, onUiEvent)
        }

        val headerCardGradient = Brush.verticalGradient(
            colors = listOf(
                if (isDarkTheme) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                },
                if (isDarkTheme) {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                } else {
                    AppColors.card
                },
            ),
        )

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
                userPresence = userPresenceDisplayData,
                selectedTabIndex = selectedTabIndex,
                onUiEvent = onUiEvent,
            )
        }

        ToggleSwitch(
            options = tabOptions,
            selectedIndex = selectedTabIndex,
            modifier = Modifier.padding(16.dp),
            onSelectedIndexChange = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    if (isDarkTheme) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        AppColors.card
                    },
                ),
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
