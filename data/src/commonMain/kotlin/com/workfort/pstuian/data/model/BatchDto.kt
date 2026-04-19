package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.BatchEntity
import kotlinx.serialization.Serializable

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
