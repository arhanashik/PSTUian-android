package com.workfort.pstuian.util.helper

import kotlin.math.floor

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 11:27 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

object MathHelper {
    inline fun <reified T>shuffle(array: ArrayList<T>) {
        var i = 0
        repeat(array.size) {
            val j = floor(Math.random() * (i + 1)).toInt()
            val temp = array[i]
            array[i] = array[j]
            array[j] = temp
            i++
        }
    }
}