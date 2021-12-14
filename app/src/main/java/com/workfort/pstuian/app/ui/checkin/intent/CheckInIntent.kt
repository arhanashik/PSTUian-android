package com.workfort.pstuian.app.ui.checkin.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:41.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class CheckInIntent {
    /**
     * If locationId is provided, get the check in list for the location
     * Else, get the check in list for logged in user
     * */
    data class GetAll(val locationId: Int, val page: Int) : CheckInIntent()
    data class GetAllByUser(val userId: Int, val userType: String, val page: Int) : CheckInIntent()
    data class CheckIn(val locationId: Int) : CheckInIntent()
    data class UpdateVisibility(val visibility: String) : CheckInIntent()
}