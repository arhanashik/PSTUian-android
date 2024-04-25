package com.workfort.pstuian.app.ui.teacherprofile

import android.content.Context
import com.workfort.pstuian.R
import com.workfort.pstuian.model.ProfileInfoItem
import com.workfort.pstuian.model.ProfileInfoItemAction
import com.workfort.pstuian.model.TeacherProfile


fun getTeacherTabs(context: Context, isSignedIn: Boolean) = arrayListOf(
    context.getString(R.string.txt_academic),
    context.getString(R.string.txt_connect),
).also {
    if (isSignedIn) {
        it.add(context.getString(R.string.txt_option))
    }
}

fun getTeacherAcademicTabItems(context: Context, profile: TeacherProfile) = listOf(
    ProfileInfoItem(context.getString(R.string.txt_name), profile.teacher.name),
    ProfileInfoItem(context.getString(R.string.txt_designation), profile.teacher.designation),
    ProfileInfoItem(context.getString(R.string.txt_department), profile.teacher.department),
    ProfileInfoItem(context.getString(R.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(context.getString(R.string.txt_blood_group), profile.teacher.blood ?: "~"),
)

fun getTeacherConnectTabItems(context: Context, profile: TeacherProfile) = listOf(
    ProfileInfoItem(context.getString(R.string.txt_address), profile.teacher.address ?: "~"),
    ProfileInfoItem(
        context.getString(R.string.txt_phone),
        profile.teacher.phone ?: "~",
        if (profile.teacher.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.teacher.phone.orEmpty())
        },
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_email),
        profile.teacher.email ?: "~",
        if (profile.teacher.email.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Email(profile.teacher.email.orEmpty())
        },
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_linked_in),
        profile.teacher.linkedIn ?: "~",
        if (profile.teacher.linkedIn.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.teacher.linkedIn.orEmpty())
        }
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_facebook),
        profile.teacher.fbLink ?: "~",
        if (profile.teacher.fbLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.teacher.fbLink.orEmpty())
        },
    ),
)

fun getTeacherOptionTabItems(context: Context) = listOf(
    ProfileInfoItem(
        context.getString(R.string.txt_password),
        context.getString(R.string.txt_change_password),
        ProfileInfoItemAction.Password,
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_devices),
        context.getString(R.string.txt_signed_in_devices),
        ProfileInfoItemAction.SignedInDevices,
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_account),
        context.getString(R.string.txt_delete_account),
        ProfileInfoItemAction.DeleteAccount,
    ),
)