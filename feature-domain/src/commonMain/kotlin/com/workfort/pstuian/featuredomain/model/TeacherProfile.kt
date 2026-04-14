package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class TeacherProfile (
    var teacher: TeacherEntity,
    var faculty: FacultyEntity,
    var isSignedIn: Boolean,
)
