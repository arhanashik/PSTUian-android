package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeProfile (
    var employee: User.Employee,
    var faculty: Faculty,
    var isSignedIn: Boolean = false,
    val isOnline: Boolean = false,
)
