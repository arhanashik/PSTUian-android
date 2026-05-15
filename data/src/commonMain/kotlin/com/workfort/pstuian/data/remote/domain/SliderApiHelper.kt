package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.SliderDto

interface SliderApiHelper {
    suspend fun getAll(): NetworkResult<List<SliderDto>>
}
