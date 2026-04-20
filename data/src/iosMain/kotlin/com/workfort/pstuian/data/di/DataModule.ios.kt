package com.workfort.pstuian.data.di

import com.workfort.pstuian.data.local.keyvaluestorage.IosKeyValueStorage
import com.workfort.pstuian.data.local.keyvaluestorage.KeyValueStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformDataModule = module {
    singleOf(::IosKeyValueStorage) bind KeyValueStorage::class
}
