package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FacultyApiService {
    @GET(Const.Remote.Api.Faculty.GET_ALL)
    suspend fun getFaculties(): Response<List<FacultyEntity>>

    @GET(Const.Remote.Api.Faculty.GET)
    suspend fun getFaculty(@Query(Const.Params.ID) id: Int): Response<FacultyEntity>

    @GET(Const.Remote.Api.BATCH.GET_ALL)
    suspend fun getBatches(@Query(Const.Params.FACULTY_ID) facultyId: Int):
            Response<List<BatchEntity>>

    @GET(Const.Remote.Api.BATCH.GET)
    suspend fun getBatch(@Query(Const.Params.ID) id: Int): Response<BatchEntity>

    @GET(Const.Remote.Api.Student.GET_ALL)
    suspend fun getStudents(
        @Query(Const.Params.FACULTY_ID) facultyId: Int,
        @Query(Const.Params.BATCH_ID) batchId: Int
    ): Response<List<StudentEntity>>

    @GET(Const.Remote.Api.Teacher.GET_ALL)
    suspend fun getTeachers(@Query(Const.Params.FACULTY_ID) facultyId: Int):
            Response<List<TeacherEntity>>

    @GET(Const.Remote.Api.Course.GET_ALL)
    suspend fun getCourseSchedules(@Query(Const.Params.FACULTY_ID) facultyId: Int):
            Response<List<CourseEntity>>

    @GET(Const.Remote.Api.Employee.GET_ALL)
    suspend fun getEmployees(@Query(Const.Params.FACULTY_ID) facultyId: Int):
            Response<List<EmployeeEntity>>
}