package com.workfort.pstuian.di

import com.workfort.pstuian.platform.UriBytesReader
import org.koin.core.module.Module
import org.koin.dsl.module

private class IosUriBytesReaderStub : UriBytesReader {
    override suspend fun readBytes(uri: String): Result<ByteArray> =
        Result.failure(Exception("Image upload from gallery is not available on iOS yet"))

    override fun suggestedFileName(uri: String): String = "profile.jpg"
}

actual val platformPresentationExtrasModule: Module = module {
    single<UriBytesReader> { IosUriBytesReaderStub() }
}
