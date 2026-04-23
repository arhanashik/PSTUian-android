package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeProfile (
    var employee: User.Employee,
    var faculty: FacultyEntity,
    var isSignedIn: Boolean = false,
)
