package com.workfort.pstuian

import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.util.PushNotificationProvider

class IosPushNotificationProvider(
    private val sharedPrefRepository: SharedPrefRepository,
) : PushNotificationProvider {
    override suspend fun getPushToken(): String? {
        return sharedPrefRepository.getString(SharedPrefKey.FCM_TOKEN)
    }
}
