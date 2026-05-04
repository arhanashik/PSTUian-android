package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class FileHandlerApiService(private val client: HttpClient) {

    suspend fun uploadImage(
        userType: String,
        filename: String,
        fileBytes: ByteArray,
    ): ApiResponse<String> {
        return client.post("file_handler.php?call=uploadImage") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("user_type", userType)
                    append("name", filename)
                    append("file", fileBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                    })
                }
            ))
        }.body()
    }

    suspend fun uploadPdf(
        filename: String,
        fileBytes: ByteArray,
    ): ApiResponse<String> {
        return client.post("file_handler.php?call=uploadPdf") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("name", filename)
                    append("file", fileBytes, Headers.build {
                        append(HttpHeaders.ContentType, "application/pdf")
                        append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                    })
                }
            ))
        }.body()
    }
}
