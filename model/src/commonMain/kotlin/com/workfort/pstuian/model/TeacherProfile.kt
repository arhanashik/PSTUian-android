package com.workfort.pstuian.model
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.dto.FacultyDto
import com.workfort.pstuian.model.dto.TeacherDto

@Serializable
data class TeacherProfile (
    var teacher: TeacherDto,
    var faculty: FacultyDto,
    var isSignedIn: Boolean,
)
