package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Donor
import kotlinx.serialization.Serializable

@Serializable
data class DonorDto (
    val id: Int,
    val name: String?,
    val info: String?,
    val email: String?,
    val reference: String
) {
    fun toModel() = Donor(
        id = id,
        name = name,
        info = info,
        email = email,
        reference = reference
    )
}

fun Donor.toDto() = DonorDto(
    id = id,
    name = name,
    info = info,
    email = email,
    reference = reference
)
