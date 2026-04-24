package com.workfort.pstuian.ui.signin.screendata

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty
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
    val faculty: Faculty?
    val email: String
    val password: String

    data class StudentSignUpFormData(
        override val name: String,
        override val faculty: Faculty? = null,
        override val email: String,
        override val password: String,
        val studentId: String,
        val regNumber: String,
        val session: String,
        val batch: Batch? = null,
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
        override val faculty: Faculty? = null,
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
