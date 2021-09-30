package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

data class TeacherResponse (val success: Boolean,
                       val message: String,
                       val teachers: ArrayList<TeacherEntity>)