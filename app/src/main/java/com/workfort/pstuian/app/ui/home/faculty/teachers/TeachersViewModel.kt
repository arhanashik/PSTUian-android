package com.workfort.pstuian.app.ui.home.faculty.teachers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.workfort.pstuian.app.data.local.database.DatabaseHelper
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherService

class TeachersViewModel : ViewModel() {
    private var teacherService: TeacherService = DatabaseHelper.provideTeacherService()
    private lateinit var teachersLiveData: LiveData<List<TeacherEntity>>

    fun getTeachers(faculty: String): LiveData<List<TeacherEntity>> {
        teachersLiveData = teacherService.getAllLive(faculty)

        return teachersLiveData
    }

    fun insertTeachers(teachers: ArrayList<TeacherEntity>) {
        Thread { teacherService.insertAll(teachers) }.start()
    }
}
