package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.remote.apihelper.FacultyApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:40 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class FacultyRepository(
    private val dbService: FacultyService,
    private val helper: FacultyApiHelper) {
    suspend fun getFaculties() : List<FacultyEntity> {
        val existingData = dbService.getAll()
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getAll()
            dbService.insertAll(newData)
            return newData
        }

        return existingData
    }
}