package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.app.data.local.student.StudentEntity

interface StudentClickEvent{
    fun onClickStudent(student: StudentEntity)
}