package com.workfort.pstuian.featuredomain.framework.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}