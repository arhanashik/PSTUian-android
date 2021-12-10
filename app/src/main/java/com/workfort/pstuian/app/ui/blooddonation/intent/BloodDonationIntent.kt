package com.workfort.pstuian.app.ui.blooddonation.intent

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 20:36.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/10.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class BloodDonationIntent {
    object GetAllDonations : BloodDonationIntent()
    object GetAllDonationRequests : BloodDonationIntent()
}