package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.DeviceApiHelper
import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.DeviceRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository

class DeviceRepositoryImpl(
    private val helper: DeviceApiHelper,
    private val sharedPrefRepository: SharedPrefRepository,
) : DeviceRepository {
    private val cache = mutableMapOf<Int, List<Device>>()

    override suspend fun getAllDevices(
        userId: String,
        userType: UserType,
        page: Int,
        useCache: Boolean,
    ): DomainResult<List<Device>> {
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) {
            return DomainResult.failure(
                DomainError(code = DomainErrorCode.Auth.DeviceNotFound),
            )
        }

        val cachedResult = cache[page]
        if (useCache && !cachedResult.isNullOrEmpty()) {
            return DomainResult.success(cachedResult)
        }

        return helper.getAllDevices(userId, userType.type, deviceId, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { cache[page] = it }
    }

    override suspend fun registerDevice(
        deviceId: String,
        model: String,
        platform: String,
        appVersionCode: Int,
        appVersionName: String,
        fcmToken: String?,
        lat: String?,
        lng: String?,
        locale: String,
    ): DomainResult<Device> {
        return helper.registerDevice(
            deviceId = deviceId,
            model = model,
            platform = platform,
            appVersionCode = appVersionCode.toString(),
            appVersionName = appVersionName,
            fcmToken = fcmToken ?: "",
            lat = lat ?: "",
            lng = lng ?: "",
            locale = locale,
        ).toDomainResult().map { it.toModel() }
    }

    override fun clearCache() {
        cache.clear()
    }
}
