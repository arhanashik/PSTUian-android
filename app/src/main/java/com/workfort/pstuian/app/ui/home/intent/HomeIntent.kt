package com.workfort.pstuian.app.ui.home.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:14 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

sealed class HomeIntent {
    data object GetSliders : HomeIntent()
    data object DeleteAllData : HomeIntent()
    data object ClearCache : HomeIntent()
}