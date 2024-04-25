package com.workfort.pstuian.model


data class StudentSignUpInput(
    val name: String,
    val id: String,
    val reg: String,
    val session: String,
    val faculty: FacultyEntity?,
    val batch: BatchEntity?,
    val email: String,
    val password: String,
) {
    companion object {
        val INITIAL = StudentSignUpInput(
            name = "",
            id = "",
            reg = "",
            session = "",
            faculty = null,
            batch = null,
            email = "",
            password = "",
        )
    }
}

data class StudentSignUpInputValidationError(
    val name: String,
    val id: String,
    val reg: String,
    val session: String,
    val faculty: String,
    val batch: String,
    val email: String,
    val password: String,
) {
    companion object {
        val INITIAL = StudentSignUpInputValidationError(
            name = "",
            id = "",
            reg = "",
            session = "",
            faculty = "",
            batch = "",
            email = "",
            password = "",
        )
    }

    fun isNotEmpty(): Boolean =
        name.isNotEmpty() ||
        id.isNotEmpty() ||
        reg.isNotEmpty() ||
        session.isNotEmpty() ||
        faculty.isNotEmpty() ||
        batch.isNotEmpty() ||
        email.isNotEmpty()
}

data class TeacherSignUpInput(
    val name: String,
    val designation: String,
    val faculty: FacultyEntity?,
    val department: String,
    val email: String,
    val password: String,
) {
    companion object {
        val INITIAL = TeacherSignUpInput(
            name = "",
            designation = "",
            faculty = null,
            department = "",
            email = "",
            password = "",
        )
    }
}

data class TeacherSignUpInputValidationError(
    val name: String,
    val designation: String,
    val faculty: String,
    val department: String,
    val email: String,
    val password: String,
) {
    companion object {
        val INITIAL = TeacherSignUpInputValidationError(
            name = "",
            designation = "",
            faculty = "",
            department = "",
            email = "",
            password = "",
        )
    }

    fun isNotEmpty(): Boolean =
        name.isNotEmpty() ||
        designation.isNotEmpty() ||
        faculty.isNotEmpty() ||
        department.isNotEmpty() ||
        email.isNotEmpty() ||
        password.isNotEmpty()
}