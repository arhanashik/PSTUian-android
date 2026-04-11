package com.workfort.pstuian.model.dto

import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.BatchEntity

@Serializable
data class BatchDto (
    val id: Int,
    val name: String,
    val title: String?,
    val session: String,
    val facultyId: Int,
    val totalStudent: Int,
    val registeredStudent: Int,
) {
    fun toEntity() = BatchEntity(
        id = id,
        name = name,
        title = title,
        session = session,
        facultyId = facultyId,
        totalStudent = totalStudent,
        registeredStudent = registeredStudent
    )
}

fun BatchEntity.toDto() = BatchDto(
    id = id,
    name = name,
    title = title,
    session = session,
    facultyId = facultyId,
    totalStudent = totalStudent,
    registeredStudent = registeredStudent
)
