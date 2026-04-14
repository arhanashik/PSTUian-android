package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeProfile (
    var employee: EmployeeEntity,
    var faculty: FacultyEntity,
    var isSignedIn: Boolean = false,
)
