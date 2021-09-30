package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.GET

interface FacultyApiService {
    @GET("faculty.php?call=getAll")
    suspend fun getFaculties(): Response<List<FacultyEntity>>
}