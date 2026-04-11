package com.workfort.pstuian.util.di

import com.workfort.pstuian.database.di.databaseModule
import com.workfort.pstuian.networking.di.networkModule
import com.workfort.pstuian.repository.di.repositoryModule
import com.workfort.pstuian.workmanager.di.workManagerModule

val appModules = listOf(
    databaseModule,
    firebaseModule,
    networkModule,
    repositoryModule,
    stateReducerModule,
    useCaseModule,
    viewModelModule,
    workManagerModule,
    helperModule,
)