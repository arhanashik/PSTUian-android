package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.SliderDto
import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.SliderApiService

class SliderApiHelperImpl(
    private val service: SliderApiService,
) : SliderApiHelper {

    override suspend fun getAll(): NetworkResult<List<SliderDto>> {
        return safeApiCall { service.getSliders() }
    }
}
