package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.SliderDto
import com.workfort.pstuian.data.remote.service.SliderApiService

class SliderApiHelper(private val service: SliderApiService) : ApiHelper<SliderDto>() {

    override suspend fun getAll(): NetworkResult<List<SliderDto>> {
        return runCatching {
            return service.getSliders().toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }
}