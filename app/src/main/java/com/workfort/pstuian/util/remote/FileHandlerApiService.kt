package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.remote.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface FileHandlerApiService {
    @Multipart
    @POST("file_handler.php?call=uploadImage")
    suspend fun uploadImage(
        @Part("user_type") userType: RequestBody,
        @Part("name") filename: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<String>

    @Multipart
    @POST("file_handler.php?call=uploadPdf")
    suspend fun uploadPdf(
        @Part("name") filename: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<String>
}