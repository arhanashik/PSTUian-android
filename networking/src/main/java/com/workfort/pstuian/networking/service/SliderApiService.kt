package com.workfort.pstuian.networking.service

import com.workfort.pstuian.model.Response
import com.workfort.pstuian.model.SliderEntity
import retrofit2.http.GET

interface SliderApiService {
    @GET("slider.php?call=getAll")
    suspend fun getSliders(): Response<List<SliderEntity>>
}