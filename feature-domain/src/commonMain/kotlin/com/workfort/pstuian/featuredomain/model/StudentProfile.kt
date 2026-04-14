package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentProfile (
    var student: StudentEntity,
    var faculty: FacultyEntity,
    var batch: BatchEntity,
    val isSignedIn: Boolean,
)
