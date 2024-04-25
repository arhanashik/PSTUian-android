package com.workfort.pstuian.workmanager.di

import com.workfort.pstuian.workmanager.usecase.domain.CompressImageUseCase
import com.workfort.pstuian.workmanager.usecase.domain.DownloadPdfUseCase
import com.workfort.pstuian.workmanager.usecase.domain.UploadImageUseCase
import com.workfort.pstuian.workmanager.usecase.domain.UploadPdfUseCase
import com.workfort.pstuian.workmanager.usecase.infrastructure.CompressImageUseCaseImpl
import com.workfort.pstuian.workmanager.usecase.infrastructure.DownloadPdfUseCaseImpl
import com.workfort.pstuian.workmanager.usecase.infrastructure.UploadImageUseCaseImpl
import com.workfort.pstuian.workmanager.usecase.infrastructure.UploadPdfUseCaseImpl
import org.koin.dsl.module

val workManagerModule = module {
    factory<CompressImageUseCase> { CompressImageUseCaseImpl() }
    factory<UploadImageUseCase> { UploadImageUseCaseImpl() }
    factory<UploadPdfUseCase> { UploadPdfUseCaseImpl() }
    factory<DownloadPdfUseCase> { DownloadPdfUseCaseImpl() }
}