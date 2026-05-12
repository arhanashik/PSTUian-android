package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

sealed interface User {
    val userId: Int
    val name: String
    val email: String
    val facultyId: Int
    val phone: String?
    val address: String?
    val bio: String?
    val blood: String?
    val imageUrl: String?

    @Serializable
    data class Student(
        override val userId: Int, // studentId
        override val name: String,
        override val email: String,
        override val facultyId: Int,
        override val phone: String?,
        override val address: String?,
        override val bio: String?,
        override val blood: String?,
        override val imageUrl: String?,
        val reg: String,
        val batchId: Int,
        val session: String,
        val linkedIn: String?,
        val fbLink: String?,
        val cvLink: String?,
    ) : User

    @Serializable
    data class Teacher(
        override val userId: Int,
        override val name: String,
        override val email: String,
        override val facultyId: Int,
        override val phone: String?,
        override val address: String?,
        override val bio: String?,
        override val blood: String?,
        override val imageUrl: String?,
        val designation: String,
        val linkedIn: String?,
        val fbLink: String?,
        val department: String,
        val description: String? = null,
    ) : User

    @Serializable
    data class Employee(
        override val userId: Int,
        override val name: String,
        override val email: String,
        override val facultyId: Int,
        override val phone: String?,
        override val address: String?,
        override val bio: String?,
        override val blood: String?,
        override val imageUrl: String?,
        val designation: String,
        val department: String?,
    ) : User
}

fun User.getUserPresenceId(): String {
    val prefix = when (this) {
        is User.Student -> UserType.STUDENT.type
        is User.Teacher -> UserType.TEACHER.type
        is User.Employee -> UserType.EMPLOYEE.type
    }

    return "$prefix-$userId"
}