package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationRequestDto

abstract class BloodDonationRequestApiHelper : ApiHelper<BloodDonationRequestDto>() {

    open suspend fun insert(
        userId: String,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestDto = throw Exception("Not implemented yet")

    open suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): BloodDonationRequestDto = throw Exception("Not implemented yet")
}