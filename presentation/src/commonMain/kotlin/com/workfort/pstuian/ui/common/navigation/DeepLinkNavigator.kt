package com.workfort.pstuian.ui.common.navigation

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.util.deeplink.DeepLinkAction

class DeepLinkNavigator(
    private val sharedScreenData: SharedScreenData,
) {

    suspend fun navigate(action: DeepLinkAction, navigator: AppNavigator?) {
        when (action) {
            is DeepLinkAction.ResetPassword -> navigator?.navigateToChangePassword(action.params)

            is DeepLinkAction.OpenProfile -> {
                if (sharedScreenData.getCurrentUser() == null) return
                val userType = UserType.fromType(action.userTypeRaw) ?: return
                navigator?.navigateToProfile(userId = action.userId, userType = userType)
            }
        }
    }
}
