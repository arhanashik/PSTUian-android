package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Donation
import kotlinx.serialization.Serializable

@Serializable
data class DonationDto (
    val id: Int,
    val name: String?,
    val email: String?,
    val reference: String,
    val message: String?,
) {
    fun toModel() = Donation(
        id = id,
        name = name,
        email = email,
        reference = reference,
        message = message,
    )
}

fun Donation.toDto() = DonationDto(
    id = id,
    name = name,
    email = email,
    reference = reference,
    message = message,
)
