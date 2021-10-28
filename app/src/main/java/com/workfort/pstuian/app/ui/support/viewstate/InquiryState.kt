package com.workfort.pstuian.app.ui.support.viewstate

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 11:26.
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

sealed class InquiryState {
    object Idle : InquiryState()
    object Loading : InquiryState()
    data class Success(val message: String) : InquiryState()
    data class Error(val error: String?) : InquiryState()
}
