package com.workfort.pstuian.util.view.animatedtextview.base

import java.util.*

/**
 *  ****************************************************************************
 *  * Created by : arhan on 26 Nov, 2021 at 23:38.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/11/26.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

object CharacterUtils {
    fun diff(oldText: CharSequence, newText: CharSequence): List<CharacterDiffResult> {
        val differentList: MutableList<CharacterDiffResult> = ArrayList()
        val skip: MutableSet<Int> = HashSet()
        for (i in oldText.indices) {
            val c = oldText[i]
            for (j in newText.indices) {
                if (!skip.contains(j) && c == newText[j]) {
                    skip.add(j)
                    differentList.add(CharacterDiffResult(c, i, j))
                    break
                }
            }
        }
        return differentList
    }

    fun needMove(index: Int, differentList: List<CharacterDiffResult>): Int {
        for ((_, fromIndex, moveIndex) in differentList) {
            if (fromIndex == index) {
                return moveIndex
            }
        }
        return -1
    }

    fun stayHere(index: Int, differentList: List<CharacterDiffResult>): Boolean {
        for ((_, _, moveIndex) in differentList) {
            if (moveIndex == index) {
                return true
            }
        }
        return false
    }

    fun getOffset(
        from: Int, move: Int, progress: Float, startX: Float, oldStartX: Float,
        gaps: List<Float>, oldGaps: List<Float>
    ): Float {
        var dist = startX
        for (i in 0 until move) {
            dist += gaps[i]
        }
        var cur = oldStartX
        for (i in 0 until from) {
            cur += oldGaps[i]
        }
        return cur + (dist - cur) * progress
    }
}