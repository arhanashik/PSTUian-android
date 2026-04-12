package com.workfort.pstuian.usecase

import com.workfort.pstuian.firebase.fcm.FcmUtil

actual object FcmTokenProvider {
    actual suspend fun getFcmToken(): String {
        return FcmUtil.getFcmToken()
    }
}
