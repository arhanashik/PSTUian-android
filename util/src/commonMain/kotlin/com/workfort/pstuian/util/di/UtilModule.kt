package com.workfort.pstuian.util.di

import com.workfort.pstuian.util.DateTimeUtil
import com.workfort.pstuian.util.DateTimeUtilImpl
import com.workfort.pstuian.util.helper.JsonParser
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformUtilModule: Module

private val utils = module {
    factoryOf(::JsonParser)
    factoryOf(::DateTimeUtilImpl) bind DateTimeUtil::class
}

val utilModule = listOf(platformUtilModule, utils)
