package com.workfort.pstuian.data.di

import com.workfort.pstuian.data.local.database.getDatabaseBuilder
import com.workfort.pstuian.data.local.keyvaluestorage.AndroidKeyValueStorage
import com.workfort.pstuian.data.local.keyvaluestorage.KeyValueStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformDataModule = module {
    singleOf(::getDatabaseBuilder)
    singleOf(::AndroidKeyValueStorage) bind KeyValueStorage::class
}
