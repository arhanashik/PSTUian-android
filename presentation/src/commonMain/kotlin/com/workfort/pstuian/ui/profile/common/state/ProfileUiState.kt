package com.workfort.pstuian.ui.profile.common.state

import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData

sealed interface ProfileUiState {
    data object None : ProfileUiState
    data object Loading : ProfileUiState
    data class Content(
        val headerDisplayData: ProfileHeaderDisplayData,
        val academicContents: List<ProfileInfoItem>,
        val connectContents: List<ProfileInfoItem>,
        val isSignedIn: Boolean = false,
        val userPresenceDisplayData: UserPresenceDisplayData = UserPresenceDisplayData(),
        val selectedTabIndex: Int = 0,
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
