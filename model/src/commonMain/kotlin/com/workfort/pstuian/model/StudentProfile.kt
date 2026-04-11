package com.workfort.pstuian.model
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.dto.BatchDto
import com.workfort.pstuian.model.dto.FacultyDto
import com.workfort.pstuian.model.dto.StudentDto

@Serializable
data class StudentProfile (
    var student: StudentDto,
    var faculty: FacultyDto,
    var batch: BatchDto,
    val isSignedIn: Boolean,
)
