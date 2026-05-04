package com.workfort.pstuian.platform

/**
 * Reads content behind a platform content [uri] (e.g. Android content://).
 */
interface UriBytesReader {
    suspend fun readBytes(uri: String): Result<ByteArray>
}
