package com.workfort.pstuian.app.ui.common.locationpicker.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 22:29.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class CheckInLocationIntent {
    data class Search(val query: String, val page: Int): CheckInLocationIntent()
    data class Get(val id: Int): CheckInLocationIntent()
    data class Create(val name: String): CheckInLocationIntent()
}
