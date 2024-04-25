package com.workfort.pstuian.model


data class TeacherAcademicInfoInputError(
    val name: String,
    val designation: String,
    val department: String,
    val bloodGroup: String,
    val faculty: String,
) {
    companion object {
        val INITIAL = TeacherAcademicInfoInputError(
            name = "",
            designation = "",
            department = "",
            bloodGroup = "",
            faculty = "",
        )
    }

    fun isNotEmpty(): Boolean = name.isNotEmpty() ||
            designation.isNotEmpty() ||
            department.isNotEmpty() ||
            bloodGroup.isNotEmpty() ||
            faculty.isNotEmpty()
}

data class TeacherConnectInfoInputError(
    val address: String,
    val phone: String,
    val email: String,
    val linkedIn: String,
    val facebook: String,
) {
    companion object {
        val INITIAL = TeacherConnectInfoInputError(
            address = "",
            phone = "",
            email = "",
            linkedIn = "",
            facebook = "",
        )
    }

    fun isNotEmpty(): Boolean = address.isNotEmpty() ||
            phone.isNotEmpty() ||
            email.isNotEmpty() ||
            linkedIn.isNotEmpty() ||
            facebook.isNotEmpty()
}

