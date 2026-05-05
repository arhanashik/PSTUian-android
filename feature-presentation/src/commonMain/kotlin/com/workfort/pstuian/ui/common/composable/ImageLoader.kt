package com.workfort.pstuian.ui.common.composable

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory

/**
 * Clears Coil memory and disk caches for the app singleton [ImageLoader].
 * Use after replacing a remote image whose URL is unchanged so the next load fetches fresh bytes.
 */
fun clearSingletonCoilImageCaches(platformContext: PlatformContext) {
    val imageLoader = SingletonImageLoader.get(platformContext)
    imageLoader.memoryCache?.clear()
    imageLoader.diskCache?.clear()
}

@Composable
fun ProvideCoilImageLoader() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }
}
