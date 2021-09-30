package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.remote.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {
    @GET("slider.php")
    fun loadSliders(): Observable<SliderResponse>

    @GET("donate.php?call=option")
    fun loadDonationOption(): Observable<DonationOptionResponse>

    @FormUrlEncoded
    @POST("donate.php?call=save")
    fun saveDonation(@Field("name") name: String,
                     @Field("info") info: String,
                     @Field("email") email: String,
                     @Field("reference") reference: String)
            : Observable<DonationSaveResponse>

    @GET("donate.php?call=donors")
    fun loadDonors(): Observable<DonorResponse>

    @GET("faculty.php")
    fun loadFaculties(): Observable<FacultyResponse>

    @GET("teacher.php")
    fun loadTeachers(@Query("faculty") faculty: String): Observable<TeacherResponse>

    @GET("batch.php")
    fun loadBatches(@Query("faculty") faculty: String): Observable<BatchResponse>

    @GET("student.php")
    fun loadStudents(@Query("faculty") faculty: String,
                     @Query("batch") batch: String): Observable<StudentResponse>

    @GET("employee.php")
    fun loadEmployees(@Query("faculty") faculty: String):
            Observable<EmployeeResponse>

    @GET("course_schedule.php")
    fun loadCourseSchedules(@Query("faculty") faculty: String):
            Observable<CourseScheduleResponse>
}