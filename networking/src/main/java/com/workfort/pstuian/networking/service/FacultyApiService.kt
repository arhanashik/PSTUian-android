package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.Response
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface FacultyApiService {
    @GET(NetworkConst.Remote.Api.Faculty.GET_ALL)
    suspend fun getFaculties(): Response<List<FacultyEntity>>

    @GET(NetworkConst.Remote.Api.Faculty.GET)
    suspend fun getFaculty(@Query(NetworkConst.Params.ID) id: Int): Response<FacultyEntity>

    @GET(NetworkConst.Remote.Api.BATCH.GET_ALL)
    suspend fun getBatches(@Query(NetworkConst.Params.FACULTY_ID) facultyId: Int):
            Response<List<BatchEntity>>

    @GET(NetworkConst.Remote.Api.BATCH.GET)
    suspend fun getBatch(@Query(NetworkConst.Params.ID) id: Int): Response<BatchEntity>

    @GET(NetworkConst.Remote.Api.Student.GET_ALL)
    suspend fun getStudents(
        @Query(NetworkConst.Params.FACULTY_ID) facultyId: Int,
        @Query(NetworkConst.Params.BATCH_ID) batchId: Int
    ): Response<List<StudentEntity>>

    @GET(NetworkConst.Remote.Api.Teacher.GET_ALL)
    suspend fun getTeachers(@Query(NetworkConst.Params.FACULTY_ID) facultyId: Int):
            Response<List<TeacherEntity>>

    @GET(NetworkConst.Remote.Api.Course.GET_ALL)
    suspend fun getCourseSchedules(@Query(NetworkConst.Params.FACULTY_ID) facultyId: Int):
            Response<List<CourseEntity>>

    @GET(NetworkConst.Remote.Api.Employee.GET_ALL)
    suspend fun getEmployees(@Query(NetworkConst.Params.FACULTY_ID) facultyId: Int):
            Response<List<EmployeeEntity>>
}