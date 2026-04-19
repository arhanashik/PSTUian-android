package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.EmployeeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDto(
    val id: Int,
    val name: String,
    val designation: String,
    val department: String?,
    val phone: String?,
    val address: String?,
    @SerialName("faculty_id")
    val facultyId: Int,
    @SerialName("image_url")
    val imageUrl: String?,
) {
    fun toEntity() = EmployeeEntity(
        id = id,
        name = name,
        designation = designation,
        department = department,
        phone = phone,
        address = address,
        facultyId = facultyId,
        imageUrl = imageUrl
    )
}

fun EmployeeEntity.toDto() = EmployeeDto(
    id = id,
    name = name,
    designation = designation,
    department = department,
    phone = phone,
    address = address,
    facultyId = facultyId,
    imageUrl = imageUrl
)
