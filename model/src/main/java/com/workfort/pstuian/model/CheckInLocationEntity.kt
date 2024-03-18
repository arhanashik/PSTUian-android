package com.workfort.pstuian.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 21:12.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

@Parcelize
data class CheckInLocationEntity (
    val id: Int,
    val name: String,
    val details: String?,
    @SerializedName("image_url")
    val imageUrl : String?,
    val link: String?,
    val verified: Int,
    val count: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_name")
    val userName: String?
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return other != null && other is CheckInLocationEntity
                && id == other.id
                && name == other.name
                && details == other.details
                && imageUrl == other.imageUrl
                && link == other.link
                && verified == other.verified
                && count == other.count
                && userId == other.userId
                && userName == other.userName
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + (details?.hashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + verified
        result = 31 * result + count
        result = 31 * result + userId
        result = 31 * result + (userName?.hashCode() ?: 0)
        return result
    }
}