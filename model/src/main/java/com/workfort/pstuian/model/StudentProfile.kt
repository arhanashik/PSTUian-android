package com.workfort.pstuian.model

data class StudentProfile (
    var student: StudentEntity,
    var faculty: FacultyEntity,
    var batch: BatchEntity,
    var isSignedIn: Boolean = false,
)