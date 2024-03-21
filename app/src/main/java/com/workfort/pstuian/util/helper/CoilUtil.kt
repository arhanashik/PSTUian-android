package com.workfort.pstuian.util.helper

import coil.imageLoader
import coil.util.CoilUtils
import com.workfort.pstuian.PstuianApp

object CoilUtil {
    fun clearCache(memory: Boolean = true, file: Boolean= true) {
        val context = PstuianApp.getBaseApplicationContext()
        // 1) clear memory cache
        if (memory) {
            val imageLoader = context.imageLoader
            imageLoader.memoryCache.clear()
        }
        // 2) clear file cache
        if (file) {
            val cache = CoilUtils.createDefaultCache(context)
            cache.directory.deleteRecursively()
        }
    }

    fun clearUrlCache(url: String) {
        val context = PstuianApp.getBaseApplicationContext()
        val cacheIterator = CoilUtils.createDefaultCache(context).urls()
        while (cacheIterator.hasNext()) {
            val cacheUrl = cacheIterator.next()
            if(cacheUrl == url) {
                cacheIterator.remove()
                return
            }
        }
    }
}