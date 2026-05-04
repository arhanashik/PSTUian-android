package com.workfort.pstuian.platform

/**
 * Reads content behind a platform content [uri] (e.g. Android content://).
 */
interface UriBytesReader {
    suspend fun readBytes(uri: String): Result<ByteArray>

    /** Filename to send to the server when the URI does not expose a useful name. */
    fun suggestedFileName(uri: String): String
}
