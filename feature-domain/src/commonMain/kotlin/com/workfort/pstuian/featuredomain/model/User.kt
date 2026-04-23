package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

sealed interface User {
    val userId: String
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
        override val userId: String,
        override val name: String,
        override val email: String,
        override val facultyId: Int,
        override val phone: String?,
        override val address: String?,
        override val bio: String?,
        override val blood: String?,
        override val imageUrl: String?,
        val studentId: String,
        val reg: String,
        val batchId: Int,
        val session: String,
        val linkedIn: String?,
        val fbLink: String?,
        val cvLink: String?,
    ) : User

    @Serializable
    data class Teacher(
        override val userId: String,
        override val name: String,
        override val email: String,
        override val facultyId: Int,
        override val phone: String?,
        override val address: String?,
        override val bio: String?,
        override val blood: String?,
        override val imageUrl: String?,
        val id: Int,
        val designation: String,
        val linkedIn: String?,
        val fbLink: String?,
        val department: String,
        val description: String? = null,
    ) : User

    @Serializable
    data class Employee(
        override val userId: String,
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