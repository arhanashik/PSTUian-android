package com.workfort.pstuian.app.ui.home.faculty.batches.students

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel;
import com.workfort.pstuian.app.data.local.database.DatabaseHelper
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.student.StudentService

class StudentsViewModel : ViewModel() {
    private var studentService: StudentService = DatabaseHelper.provideStudentService()
    private lateinit var studentsLiveData: LiveData<List<StudentEntity>>

    fun getStudents(faculty: String, batch: String): LiveData<List<StudentEntity>> {
        studentsLiveData = studentService.getAllLive(faculty, batch)

        return studentsLiveData
    }

    fun insertStudents(students: ArrayList<StudentEntity>) {
        Thread { studentService.insertAll(students) }.start()
    }
}
