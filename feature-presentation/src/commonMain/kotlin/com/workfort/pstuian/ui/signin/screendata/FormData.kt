package com.workfort.pstuian.ui.signin.screendata

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.util.isValidEmail

data class SignInFormData(
    val email: String,
    val password: String,
) {
    fun isInvalid(): Boolean = email.isEmpty() ||
            email.isValidEmail().not() ||
            password.isEmpty() ||
            password.length < 6
}

sealed interface SignUpFormData {
    val name: String
    val faculty: FacultyEntity?
    val email: String
    val password: String

    data class StudentSignUpFormData(
        override val name: String,
        override val faculty: FacultyEntity? = null,
        override val email: String,
        override val password: String,
        val studentId: String,
        val regNumber: String,
        val session: String,
        val batch: BatchEntity? = null,
    ) : SignUpFormData {

        fun isInvalid(): Boolean = name.isEmpty() ||
                email.isEmpty() ||
                email.isValidEmail().not() ||
                studentId.isEmpty() ||
                regNumber.isEmpty() ||
                session.isEmpty() ||
                faculty == null ||
                batch == null ||
                password.isEmpty() ||
                password.length < 6
    }

    data class TeacherSignUpFormData(
        override val name: String,
        override val faculty: FacultyEntity? = null,
        override val email: String,
        override val password: String,
        val department: String,
        val designation: String,
    ) : SignUpFormData {

        fun isInvalid(): Boolean = name.isEmpty() ||
                email.isEmpty() ||
                email.isValidEmail().not() ||
                faculty == null ||
                department.isEmpty() ||
                designation.isEmpty() ||
                password.isEmpty() ||
                password.length < 6
    }
}
