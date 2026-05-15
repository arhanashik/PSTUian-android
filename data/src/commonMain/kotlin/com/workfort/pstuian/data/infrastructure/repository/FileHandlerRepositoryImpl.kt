package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.remote.service.FileHandlerApiService
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.FileHandlerRepository

class FileHandlerRepositoryImpl(
    private val fileHandlerApiService: FileHandlerApiService,
) : FileHandlerRepository {

    override suspend fun uploadImage(
        userType: UserType,
        filename: String,
        fileBytes: ByteArray,
    ): DomainResult<String> {
        return fileHandlerApiService.uploadImage(userType.type, filename, fileBytes)
            .toNetworkResult()
            .toDomainResult()
    }

    override suspend fun uploadCv(
        filename: String,
        fileBytes: ByteArray
    ): DomainResult<String> {
        return fileHandlerApiService.uploadPdf(filename, fileBytes)
            .toNetworkResult()
            .toDomainResult()
    }
}
