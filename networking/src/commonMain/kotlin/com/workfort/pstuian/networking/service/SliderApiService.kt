package com.workfort.pstuian.networking.service

import com.workfort.pstuian.model.dto.SliderDto
import com.workfort.pstuian.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class SliderApiService(private val client: HttpClient) {
    suspend fun getSliders(): Response<List<SliderDto>> {
        return client.get("slider.php?call=getAll").body()
    }
}
