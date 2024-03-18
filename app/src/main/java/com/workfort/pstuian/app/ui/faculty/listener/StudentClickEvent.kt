package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.model.StudentEntity

interface StudentClickEvent{
    fun onClickStudent(student: StudentEntity)
}