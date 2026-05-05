package com.workfort.pstuian.util.di

import com.workfort.pstuian.util.DateTimeUtil
import com.workfort.pstuian.util.DateTimeUtilImpl
import com.workfort.pstuian.util.deeplink.DeepLinkParser
import com.workfort.pstuian.util.helper.JsonParser
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private val utils = module {
    factoryOf(::JsonParser)
    factoryOf(::DateTimeUtilImpl) bind DateTimeUtil::class
    singleOf(::DeepLinkParser)
}

val utilModule = listOf(utils, platformFileImageModule)
