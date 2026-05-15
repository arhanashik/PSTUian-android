package com.workfort.pstuian.data.remote

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult

/**
 * Runs a suspend service call that returns [ApiResponse] and maps it to [NetworkResult].
 */
suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> ApiResponse<T>,
): NetworkResult<T> {
    return runCatching { block().toNetworkResult() }.getOrElse {
        NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
    }
}
