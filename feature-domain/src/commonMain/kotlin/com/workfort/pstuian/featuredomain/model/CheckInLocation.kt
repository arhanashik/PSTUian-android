package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckInLocation (
    val id: Int,
    val name: String,
    val details: String?,
    val imageUrl : String?,
    val link: String?,
    val verified: Int,
    val count: Int,
    val userId: Int,
    val userType: String?
) {
    override fun equals(other: Any?): Boolean {
        return other != null && other is CheckInLocation
                && id == other.id
                && name == other.name
                && details == other.details
                && imageUrl == other.imageUrl
                && link == other.link
                && verified == other.verified
                && count == other.count
                && userId == other.userId
                && userType == other.userType
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
        result = 31 * result + (userType?.hashCode() ?: 0)
        return result
    }
}
