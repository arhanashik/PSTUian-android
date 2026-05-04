package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface FileHandlerRepository {

    suspend fun uploadImage(
        userType: UserType,
        filename: String,
        fileBytes: ByteArray,
    ): DomainResult<String>
}
