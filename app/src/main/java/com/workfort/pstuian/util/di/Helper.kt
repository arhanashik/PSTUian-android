package com.workfort.pstuian.util.di

import com.workfort.pstuian.util.helper.AndroidGsonUtil
import com.workfort.pstuian.util.helper.GsonUtil
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val helperModule = module {
    factoryOf<GsonUtil>(::AndroidGsonUtil)
}