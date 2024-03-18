package com.workfort.pstuian.app.ui.studentprofile.intent

import com.workfort.pstuian.model.StudentEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 20 Oct, 2021 at 4:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

sealed class StudentProfileIntent {
    data class GetProfile(val id: Int) : StudentProfileIntent()
    data class ChangeProfileImage(
        val student: StudentEntity,
        val imageUrl: String,
    ) : StudentProfileIntent()
    data class ChangeName(val student: StudentEntity, val newName: String) : StudentProfileIntent()
    data class ChangeBio(val student: StudentEntity, val newBio: String) : StudentProfileIntent()
    data class ChangeAcademicInfo(
        val student: StudentEntity,
        val name: String,
        val id: Int,
        val reg: String,
        val blood: String,
        val facultyId: Int,
        val session: String,
        val batchId: Int
    ) : StudentProfileIntent()
    data class ChangeConnectInfo(
        val student: StudentEntity,
        val address: String,
        val phone: String,
        val email: String,
        val cvLink: String,
        val linkedIn: String,
        val facebook: String
    ) : StudentProfileIntent()
}