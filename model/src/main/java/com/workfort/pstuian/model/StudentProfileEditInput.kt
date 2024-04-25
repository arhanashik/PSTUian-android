package com.workfort.pstuian.model


data class StudentAcademicInfoInputError(
    val name: String,
    val id: String,
    val reg: String,
    val bloodGroup: String,
    val faculty: String,
    val batch: String,
    val session: String,
) {
    companion object {
        val INITIAL = StudentAcademicInfoInputError(
            name = "",
            id = "",
            reg = "",
            bloodGroup = "",
            faculty = "",
            batch = "",
            session = "",
        )
    }

    fun isNotEmpty(): Boolean = name.isNotEmpty() ||
            id.isNotEmpty() ||
            reg.isNotEmpty() ||
            bloodGroup.isNotEmpty() ||
            faculty.isNotEmpty() ||
            batch.isNotEmpty() ||
            session.isNotEmpty()
}

data class StudentConnectInfoInputError(
    val address: String,
    val phone: String,
    val email: String,
    val cvLink: String,
    val linkedIn: String,
    val facebook: String,
) {
    companion object {
        val INITIAL = StudentConnectInfoInputError(
            address = "",
            phone = "",
            email = "",
            cvLink = "",
            linkedIn = "",
            facebook = "",
        )
    }

    fun isNotEmpty(): Boolean = address.isNotEmpty() ||
            phone.isNotEmpty() ||
            email.isNotEmpty() ||
            cvLink.isNotEmpty() ||
            linkedIn.isNotEmpty() ||
            facebook.isNotEmpty()
}