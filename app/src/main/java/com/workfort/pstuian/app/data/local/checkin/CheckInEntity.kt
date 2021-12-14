package com.workfort.pstuian.app.data.local.checkin

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:06.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

@Parcelize
data class CheckInEntity(
    val id: Int,
    @SerializedName("location_id")
    val locationId: Int,
    @SerializedName("location_name")
    val locationName: String,
    val count: Int,
    val visibility: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_type")
    val userType: String,
    val name: String,
    val batch: String,
    val phone: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("created_at")
    val date: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return other != null && other is CheckInEntity
                && id == other.id
                && locationId == other.locationId
                && locationName == other.locationName
                && count == other.count
                && visibility == other.visibility
                && userId == other.userId
                && userType == other.userType
                && name == other.name
                && batch == other.batch
                && phone == other.phone
                && imageUrl == other.imageUrl
                && date == other.date
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + locationId
        result = 31 * result + locationName.hashCode()
        result = 31 * result + count
        result = 31 * result + visibility.hashCode()
        result = 31 * result + userId
        result = 31 * result + userType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + batch.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + date.hashCode()
        return result
    }
}
