package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

sealed interface UserProfile {
    val faculty: Faculty
    val isSignedIn: Boolean
    val isOnline: Boolean

    @Serializable
    data class StudentProfile (
        val student: User.Student,
        override val faculty: Faculty,
        val batch: Batch,
        override val isSignedIn: Boolean = false,
        override val isOnline: Boolean = false,
    ) : UserProfile

    @Serializable
    data class TeacherProfile (
        val teacher: User.Teacher,
        override val faculty: Faculty,
        override val isSignedIn: Boolean = false,
        override val isOnline: Boolean = false,
    ) : UserProfile

    @Serializable
    data class EmployeeProfile (
        val employee: User.Employee,
        override val faculty: Faculty,
        override val isSignedIn: Boolean = false,
        override val isOnline: Boolean = false,
    ): UserProfile
}
