package com.workfort.pstuian.featuredomain.network

import com.workfort.pstuian.featuredomain.model.DomainResult

/**
 * Downloads CV bytes from an absolute HTTPS (or HTTP) URL.
 */
interface CvPdfRemoteFetcher {
    suspend fun fetchPdfBytes(url: String): DomainResult<ByteArray>
}
