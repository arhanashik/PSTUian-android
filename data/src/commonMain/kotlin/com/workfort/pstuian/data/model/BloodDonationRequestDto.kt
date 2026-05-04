package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BloodDonationRequestDto(
    val id: Int,
    @SerialName("blood_group")
    val bloodGroup: String,
    @SerialName("before_date")
    val beforeDate: String,
    @SerialName("contact")
    val contacts: String,
    val info: String?,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_type")
    val userType: String,
    val name: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val confirmed: Int = 0,
    val completed: Int = 0,
) {
    fun toModel() = BloodDonationRequest(
        id = id,
        bloodGroup = bloodGroup,
        beforeDate = beforeDate,
        contacts = contacts,
        info = info,
        userId = userId,
        userType = userType,
        name = name,
        imageUrl = imageUrl,
        confirmed = confirmed == 1,
        completed = completed == 1,
    )
}

fun BloodDonationRequest.toDto() = BloodDonationRequestDto(
    id = id,
    bloodGroup = bloodGroup,
    beforeDate = beforeDate,
    contacts = contacts,
    info = info,
    userId = userId,
    userType = userType,
    name = name,
    imageUrl = imageUrl,
    confirmed = if (confirmed) 1 else 0,
    completed = if (completed) 1 else 0,
)
