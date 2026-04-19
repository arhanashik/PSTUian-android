package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.SliderDto
import com.workfort.pstuian.data.model.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SliderApiService(private val client: HttpClient) {
    suspend fun getSliders(): ApiResponse<List<SliderDto>> {
        return client.get("slider.php?call=getAll").body()
    }
}
