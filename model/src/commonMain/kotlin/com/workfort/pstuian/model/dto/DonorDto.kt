package com.workfort.pstuian.model.dto

import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.DonorEntity

@Serializable
data class DonorDto (
    val id: Int,
    val name: String?,
    val info: String?,
    val email: String?,
    val reference: String
) {
    fun toEntity() = DonorEntity(
        id = id,
        name = name,
        info = info,
        email = email,
        reference = reference
    )
}

fun DonorEntity.toDto() = DonorDto(
    id = id,
    name = name,
    info = info,
    email = email,
    reference = reference
)
