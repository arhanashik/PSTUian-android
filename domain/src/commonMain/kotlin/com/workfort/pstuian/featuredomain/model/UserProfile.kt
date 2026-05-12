package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

sealed interface UserProfile {
    val user: User
    val faculty: Faculty
    val isSignedIn: Boolean

    @Serializable
    data class StudentProfile (
        override val user: User.Student,
        override val faculty: Faculty,
        override val isSignedIn: Boolean = false,
        val batch: Batch,
    ) : UserProfile

    @Serializable
    data class TeacherProfile (
        override val user: User.Teacher,
        override val faculty: Faculty,
        override val isSignedIn: Boolean = false,
    ) : UserProfile

    @Serializable
    data class EmployeeProfile (
        override val user: User.Employee,
        override val faculty: Faculty,
        override val isSignedIn: Boolean = false,
    ) : UserProfile
}
