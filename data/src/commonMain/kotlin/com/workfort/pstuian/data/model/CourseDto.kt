package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Course
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseDto (
    val id: Int,
    @SerialName("course_code")
    val courseCode: String,
    @SerialName("course_title")
    val courseTitle: String,
    @SerialName("credit_hour")
    val creditHour: String,
    @SerialName("faculty_id")
    val facultyId: Int,
    val status: Int,
) {
    fun toModel() = Course(
        id = id,
        courseCode = courseCode,
        courseTitle = courseTitle,
        creditHour = creditHour,
        facultyId = facultyId,
        status = status
    )
}

fun Course.toDto() = CourseDto(
    id = id,
    courseCode = courseCode,
    courseTitle = courseTitle,
    creditHour = creditHour,
    facultyId = facultyId,
    status = status
)
