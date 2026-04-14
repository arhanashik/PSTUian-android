package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 17:13.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

@Serializable
data class BloodDonationRequestEntity(
    val id: Int,
    val bloodGroup: String,
    val beforeDate: String,
    val contacts: String,
    val info: String?,
    val userId: String,
    val userType: String,
    val name: String,
    val imageUrl: String?,
) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is BloodDonationRequestEntity
                && id == other.id
                && bloodGroup == other.bloodGroup
                && beforeDate == other.beforeDate
                && contacts == other.contacts
                && info == other.info
                && userId == other.userId
                && userType == other.userType
                && name == other.name
                && imageUrl == other.imageUrl
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + bloodGroup.hashCode()
        result = 31 * result + beforeDate.hashCode()
        result = 31 * result + contacts.hashCode()
        result = 31 * result + (info?.hashCode() ?: 0)
        result = 31 * result + userId.hashCode()
        result = 31 * result + userType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        return result
    }
}
