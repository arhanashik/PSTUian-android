package com.workfort.pstuian.app.ui.splash.viewstate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 19:01.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/28.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class ClearCacheState {
    object Idle : ClearCacheState()
    object Loading : ClearCacheState()
    object Success : ClearCacheState()
    data class Error(val error: String?) : ClearCacheState()
}
