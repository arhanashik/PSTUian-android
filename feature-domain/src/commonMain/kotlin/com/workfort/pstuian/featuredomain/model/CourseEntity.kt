package com.workfort.pstuian.featuredomain.model

data class CourseEntity (
    var id: Int,
    var courseCode: String,
    var courseTitle: String,
    var creditHour: String,
    var facultyId: Int,
    var status: Int,
)
