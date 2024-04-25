package com.workfort.pstuian.app.ui.employeeprofile

import android.content.Context
import com.workfort.pstuian.R
import com.workfort.pstuian.model.EmployeeProfile
import com.workfort.pstuian.model.ProfileInfoItem
import com.workfort.pstuian.model.ProfileInfoItemAction


fun getEmployeeAcademicTabItems(context: Context, profile: EmployeeProfile) = listOf(
    ProfileInfoItem(
        context.getString(R.string.txt_designation),
        profile.emplyee.designation,
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_department),
        profile.emplyee.department ?: "~",
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_faculty),
        profile.faculty.title,
    ),
)

fun getEmployeeConnectTabItems(context: Context, profile: EmployeeProfile) = listOf(
    ProfileInfoItem(
        context.getString(R.string.txt_address),
        profile.emplyee.address ?: "~"
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_phone),
        profile.emplyee.phone ?: "~",
        if (profile.emplyee.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.emplyee.phone.orEmpty())
        },
    ),
)