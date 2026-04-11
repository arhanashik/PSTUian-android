package com.workfort.pstuian.model
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.dto.EmployeeDto
import com.workfort.pstuian.model.dto.FacultyDto

@Serializable
data class EmployeeProfile (
    var employee: EmployeeDto,
    var faculty: FacultyDto,
)
