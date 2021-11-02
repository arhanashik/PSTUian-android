package com.workfort.pstuian.app.ui.donors.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 07:51 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/01/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class DonorsIntent {
    object GetDonors : DonorsIntent()
    object GetDonationOptions : DonorsIntent()
}