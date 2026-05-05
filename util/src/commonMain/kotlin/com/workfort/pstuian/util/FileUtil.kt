package com.workfort.pstuian.util

/**
 * Utilities for platform content/file [uri] strings (e.g. Android content://, iOS file URLs).
 */
interface FileUtil {
    suspend fun readBytes(uri: String): Result<ByteArray>

    suspend fun writeBytes(destinationUri: String, bytes: ByteArray): Result<Unit>

    suspend fun getFileName(uri: String): Result<String>

    companion object {
        /** Display / upload fallback when the original name cannot be resolved. */
        const val FALLBACK_DOCUMENT_NAME = "document.pdf"
    }
}
