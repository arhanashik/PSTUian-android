package com.workfort.pstuian.model


data class TeacherProfile(
    var teacher: TeacherEntity,
    var faculty: FacultyEntity,
    var isSignedIn: Boolean = false
)
