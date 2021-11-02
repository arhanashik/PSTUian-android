package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.GET

interface SliderApiService {
    @GET("slider.php?call=getAll")
    suspend fun getSliders(): Response<List<SliderEntity>>
}