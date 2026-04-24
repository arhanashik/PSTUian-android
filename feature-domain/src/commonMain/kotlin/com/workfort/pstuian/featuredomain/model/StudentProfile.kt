package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentProfile (
    val student: User.Student,
    val faculty: Faculty,
    val batch: Batch,
    val isSignedIn: Boolean,
)
