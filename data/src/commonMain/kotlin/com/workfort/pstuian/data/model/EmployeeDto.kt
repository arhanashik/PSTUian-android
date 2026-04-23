package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.User
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
    fun toModel() = User.Employee(
        userId = id.toString(),
        name = name,
        email = "",
        facultyId = facultyId,
        phone = phone,
        address = address,
        bio = null,
        blood = null,
        imageUrl = imageUrl,
        designation = designation,
        department = department,
    )
}

fun User.Employee.toDto() = EmployeeDto(
    id = userId.toIntOrNull() ?: 0,
    name = name,
    designation = designation,
    department = department,
    phone = phone,
    address = address,
    facultyId = facultyId,
    imageUrl = imageUrl
)
