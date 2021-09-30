package com.workfort.pstuian.app.ui.home.faculty.listener

import com.workfort.pstuian.app.data.local.faculty.FacultyEntity

interface FacultyClickEvent{
    fun onClickFaculty(faculty: FacultyEntity)
}