package com.workfort.pstuian.app.ui.splash.viewstate

import com.workfort.pstuian.app.data.local.config.ConfigEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 15 Oct, 2021 at 9:19 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/15/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class ConfigState {
    object Idle : ConfigState()
    object Loading : ConfigState()
    data class Success(val config: ConfigEntity) : ConfigState()
    data class Error(val error: String?) : ConfigState()
}
