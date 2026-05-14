package com.workfort.pstuian.featuredomain.network

import com.workfort.pstuian.featuredomain.model.DomainResult

/**
 * Downloads File bytes from an absolute HTTPS (or HTTP) URL.
 */
interface FileRemoteFetcher {
    suspend fun fetchFileBytes(url: String): DomainResult<ByteArray>
}
