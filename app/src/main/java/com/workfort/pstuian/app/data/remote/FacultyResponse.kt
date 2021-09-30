package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.faculty.FacultyEntity

data class FacultyResponse(val success: Boolean,
                           val message: String,
                           val faculties: ArrayList<FacultyEntity>)
