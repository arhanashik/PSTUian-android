package com.workfort.pstuian.app.data.local.blooddonationrequest

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 17:13.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/10.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

@Parcelize
data class BloodDonationRequestEntity(
    val id: Int,
    @SerializedName("blood_group")
    val bloodGroup: String,
    @SerializedName("before_date")
    val beforeDate: String,
    @SerializedName("contact")
    val contacts: String,
    @SerializedName("info")
    val info: String?,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val imageUrl: String?,
) : Parcelable {
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
