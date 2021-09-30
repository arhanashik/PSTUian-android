package com.workfort.pstuian.app.ui.home.faculty.listener

import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

interface TeacherClickEvent{
    fun onClickTeacher(teacher: TeacherEntity)
}