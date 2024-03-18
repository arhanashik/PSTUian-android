package com.workfort.pstuian.workmanager.di

import com.workfort.pstuian.workmanager.FileHandlerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val workManagerModule = module {
    viewModel { FileHandlerViewModel() }
}