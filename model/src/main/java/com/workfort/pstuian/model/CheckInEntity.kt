package com.workfort.pstuian.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckInEntity(
    val id: Int,
    @SerializedName("location_id")
    val locationId: Int,
    @SerializedName("location_name")
    val locationName: String,
    @SerializedName("location_image_url")
    val locationImageUrl: String?,
    val count: Int,
    val privacy: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_type")
    val userType: String,
    val name: String,
    val batch: String,
    val phone: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val date: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return other != null && other is CheckInEntity
                && id == other.id
                && locationId == other.locationId
                && locationName == other.locationName
                && count == other.count
                && privacy == other.privacy
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
        result = 31 * result + privacy.hashCode()
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
