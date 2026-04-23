package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class TeacherProfile (
    var teacher: User.Teacher,
    var faculty: FacultyEntity,
    var isSignedIn: Boolean,
)
