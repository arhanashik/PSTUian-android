package com.workfort.pstuian.app.ui.home.viewstate

import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.slider.SliderEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:15 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class SliderState {
    object Idle : SliderState()
    object Loading : SliderState()
    data class Sliders(val sliders: List<SliderEntity>) : SliderState()
    data class Error(val error: String?) : SliderState()
}

sealed class FacultyState {
    object Idle : FacultyState()
    object Loading : FacultyState()
    data class Faculties(val faculties: List<FacultyEntity>) : FacultyState()
    data class Error(val error: String?) : FacultyState()
}