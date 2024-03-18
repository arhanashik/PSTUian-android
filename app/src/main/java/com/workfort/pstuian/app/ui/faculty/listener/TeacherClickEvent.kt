package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.model.TeacherEntity

interface TeacherClickEvent{
    fun onClickTeacher(teacher: TeacherEntity)
}