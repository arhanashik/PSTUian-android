package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata.BloodDonationRequestDisplayData


class BloodDonationRequestDisplayDataMapper {

    fun map(
        userId: Int,
        userType: UserType,
        requestList: List<BloodDonationRequest>
    ): List<BloodDonationRequestDisplayData> {
        return requestList.map { request ->
            BloodDonationRequestDisplayData(
                bloodDonationRequest = request,
                contacts = request.contacts.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                needBeforeFormattedDate = request.beforeDate.split(" ").firstOrNull() ?: request.beforeDate,
                isOwnItem = request.userId == userId && request.userType == userType.type,
            )
        }
    }
}