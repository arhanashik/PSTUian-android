package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FacultyApiService {
    @GET("faculty.php?call=getAll")
    suspend fun getFaculties(): Response<List<FacultyEntity>>

    @GET("batch.php?call=getAll")
    suspend fun getBatches(@Query("faculty_id") facultyId: Int):
            Response<List<BatchEntity>>

    @GET("batch.php?call=get")
    suspend fun getBatch(@Query("id") id: Int): Response<BatchEntity>

    @GET("student.php?call=getAll")
    suspend fun getStudents(@Query("faculty_id") facultyId: Int,
                    @Query("batch_id") batchId: Int): Response<List<StudentEntity>>

    @GET("teacher.php?call=getAll")
    suspend fun getTeachers(@Query("faculty_id") facultyId: Int):
            Response<List<TeacherEntity>>

    @GET("course.php?call=getAll")
    suspend fun getCourseSchedules(@Query("faculty_id") facultyId: Int):
            Response<List<CourseEntity>>

    @GET("employee.php?call=getAll")
    suspend fun getEmployees(@Query("faculty_id") facultyId: Int):
            Response<List<EmployeeEntity>>
}