package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.network.FileRemoteFetcher
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileRemoteFetcherImpl(
    private val httpClient: HttpClient,
) : FileRemoteFetcher {

    override suspend fun fetchFileBytes(url: String): DomainResult<ByteArray> =
        withContext(Dispatchers.Default) {
        val trimmed = url.trim()
        if (!trimmed.startsWith("https://", ignoreCase = true) &&
            !trimmed.startsWith("http://", ignoreCase = true)
        ) {
            return@withContext DomainResult.failure(DomainError(DomainErrorCode.Validation.InputInvalid))
        }
        runCatching {
            val response = httpClient.get(trimmed)
            val statusCode = response.status.value
            if (statusCode !in 200..<300) {
                error("Server returned $statusCode")
            }
            response.readRawBytes()
        }.fold(
            onSuccess = { DomainResult.success(it) },
            onFailure = { err ->
                DomainResult.failure(
                    DomainError(DomainErrorCode.File.DownloadFailed, err),
                )
            },
        )
    }
}
