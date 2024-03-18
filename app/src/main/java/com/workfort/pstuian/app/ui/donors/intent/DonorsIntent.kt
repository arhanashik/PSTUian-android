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
 *  ****************************************************************************
 */

sealed class DonorsIntent {
    object GetDonors : DonorsIntent()
    object GetDonationOptions : DonorsIntent()
}