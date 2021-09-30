package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.util.remote.FacultyApiService
import com.workfort.pstuian.util.remote.SliderApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 12:58 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */
class FacultyApiHelper(private val service: FacultyApiService) : ApiHelper<FacultyEntity> {
    override suspend fun getAll(): List<FacultyEntity> {
        val response = service.getFaculties()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}