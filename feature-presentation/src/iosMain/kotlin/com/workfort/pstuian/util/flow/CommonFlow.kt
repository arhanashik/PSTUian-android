package com.workfort.pstuian.util.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * A wrapper for [Flow] to be used in iOS/Swift.
 */
class CommonFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = origin.onEach { block(it) }.launchIn(CoroutineScope(Dispatchers.Main))
        return Closeable { job.cancel() }
    }
}

fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)

fun interface Closeable {
    fun close()
}
