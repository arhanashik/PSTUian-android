package com.workfort.pstuian.featuredomain.framework.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

fun CoroutineScope.launchOnMain(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    block: suspend CoroutineScope.() -> Unit,
) = launch(
    context = coroutineDispatcherProvider.main,
    start = CoroutineStart.DEFAULT,
    block = block,
)