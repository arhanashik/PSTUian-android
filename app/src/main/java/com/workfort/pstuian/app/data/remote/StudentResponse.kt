package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.student.StudentEntity

data class StudentResponse (val success: Boolean,
                            val message: String,
                            val students: ArrayList<StudentEntity>)