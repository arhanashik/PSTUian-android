package com.workfort.pstuian.ui.profile.common.state

import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem

sealed interface ProfileUiState {
    data object None : ProfileUiState
    data object Loading : ProfileUiState
    data class Content(
        val headerDisplayData: ProfileHeaderDisplayData,
        val academicContents: List<ProfileInfoItem>,
        val connectContents: List<ProfileInfoItem>,
        val isSignedIn: Boolean,
        val isOnline: Boolean,
        val selectedTabIndex: Int,
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
