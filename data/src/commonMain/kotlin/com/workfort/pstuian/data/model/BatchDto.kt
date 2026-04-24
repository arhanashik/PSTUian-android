package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Batch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BatchDto (
    val id: Int,
    val name: String,
    val title: String?,
    val session: String,
    @SerialName("faculty_id")
    val facultyId: Int,
    @SerialName("total_student")
    val totalStudent: Int = 0,
    @SerialName("registered_student")
    val registeredStudent: Int = 0,
) {
    fun toModel() = Batch(
        id = id,
        name = name,
        title = title,
        session = session,
        facultyId = facultyId,
        totalStudent = totalStudent,
        registeredStudent = registeredStudent
    )
}

fun Batch.toDto() = BatchDto(
    id = id,
    name = name,
    title = title,
    session = session,
    facultyId = facultyId,
    totalStudent = totalStudent,
    registeredStudent = registeredStudent
)
