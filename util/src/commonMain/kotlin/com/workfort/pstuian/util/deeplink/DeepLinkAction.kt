package com.workfort.pstuian.util.deeplink

sealed interface DeepLinkAction {

    data class ResetPassword(
        val params: ResetPasswordParams,
    ) : DeepLinkAction

    data class OpenProfile(
        val userId: Int,
        val userTypeRaw: String,
    ) : DeepLinkAction
}
