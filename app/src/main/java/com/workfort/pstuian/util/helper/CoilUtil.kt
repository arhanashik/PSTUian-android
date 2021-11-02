package com.workfort.pstuian.util.helper

import coil.imageLoader
import coil.util.CoilUtils
import com.workfort.pstuian.PstuianApp

/**
 *  ****************************************************************************
 *  * Created by : arhan on 22 Oct, 2021 at 2:17.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/22.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

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
}