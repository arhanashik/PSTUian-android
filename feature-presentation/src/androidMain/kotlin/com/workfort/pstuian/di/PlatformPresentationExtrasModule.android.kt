package com.workfort.pstuian.di

import android.content.Context
import android.net.Uri
import com.workfort.pstuian.platform.UriBytesReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import androidx.core.net.toUri

private class AndroidUriBytesReader(
    private val context: Context,
) : UriBytesReader {

    override suspend fun readBytes(uri: String): Result<ByteArray> = withContext(Dispatchers.IO) {
        runCatching {
            val parsed = uri.toUri()
            context.contentResolver.openInputStream(parsed)?.use { stream -> stream.readBytes() }
                ?: error("Could not open the selected image")
        }
    }

    override fun suggestedFileName(uri: String): String {
        val parsed = uri.toUri()
        val fromPath = parsed.path?.substringAfterLast('/')?.takeIf { it.isNotEmpty() }
        val fromLastSegment = parsed.lastPathSegment?.takeIf { it.isNotEmpty() }
        return (fromPath ?: fromLastSegment)?.let { name ->
            if (name.contains('.')) name else "$name.jpg"
        } ?: "profile.jpg"
    }
}

actual val platformPresentationExtrasModule: Module = module {
    single<UriBytesReader> { AndroidUriBytesReader(androidContext()) }
}
