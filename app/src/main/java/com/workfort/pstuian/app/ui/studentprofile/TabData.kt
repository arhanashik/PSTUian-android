package com.workfort.pstuian.app.ui.studentprofile

import android.content.Context
import com.workfort.pstuian.R
import com.workfort.pstuian.model.ProfileInfoItem
import com.workfort.pstuian.model.ProfileInfoItemAction
import com.workfort.pstuian.model.StudentProfile


fun getStudentsTabs(context: Context, isSignedIn: Boolean) = arrayListOf(
    context.getString(R.string.txt_academic),
    context.getString(R.string.txt_connect),
).also {
    if (isSignedIn) {
        it.add(context.getString(R.string.txt_option))
    }
}

fun getStudentAcademicTabItems(context: Context, profile: StudentProfile) = listOf(
    ProfileInfoItem(context.getString(R.string.txt_name), profile.student.name),
    ProfileInfoItem(context.getString(R.string.txt_id), profile.student.id.toString()),
    ProfileInfoItem(context.getString(R.string.txt_registration_number), profile.student.reg),
    ProfileInfoItem(context.getString(R.string.txt_blood_group), profile.student.blood ?: "~"),
    ProfileInfoItem(context.getString(R.string.txt_faculty), profile.faculty.title),
    ProfileInfoItem(context.getString(R.string.txt_batch), profile.batch.name),
    ProfileInfoItem(context.getString(R.string.txt_session), profile.student.session),
)

fun getStudentConnectTabItems(context: Context, profile: StudentProfile) = listOf(
    ProfileInfoItem(context.getString(R.string.txt_address), profile.student.address ?: "~"),
    ProfileInfoItem(
        context.getString(R.string.txt_phone),
        profile.student.phone ?: "~",
        if (profile.student.phone.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Call(profile.student.phone.orEmpty())
        },
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_email),
        profile.student.email ?: "~",
        if (profile.student.email.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Email(profile.student.email.orEmpty())
        },
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_cv),
        profile.student.cvLink ?: "~",
        if (profile.student.cvLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.DownloadCv(profile.student.cvLink.orEmpty())
        }
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_linked_in),
        profile.student.linkedIn ?: "~",
        if (profile.student.linkedIn.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.student.linkedIn.orEmpty())
        }
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_facebook),
        profile.student.fbLink ?: "~",
        if (profile.student.fbLink.isNullOrEmpty()) {
            ProfileInfoItemAction.None
        } else {
            ProfileInfoItemAction.Link(profile.student.fbLink.orEmpty())
        },
    ),
)

fun getStudentOptionTabItems(context: Context) = listOf(
    ProfileInfoItem(
        context.getString(R.string.txt_password),
        context.getString(R.string.txt_change_password),
        ProfileInfoItemAction.Password,
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_cv),
        context.getString(R.string.hint_upload_new_cv),
        ProfileInfoItemAction.UploadCv,
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_blood_donation),
        context.getString(R.string.txt_my_donation_list),
        ProfileInfoItemAction.BloodDonationList,
    ),
    ProfileInfoItem(
        context.getString(R.string.txt_check_in),
        context.getString(R.string.txt_my_check_in_list),
        ProfileInfoItemAction.CheckInList,
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