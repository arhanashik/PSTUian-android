package com.workfort.pstuian.ui.profile.studentprofile

import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction


class StudentProfileDisplayDataMapper {

    fun mapHeaderData(profile: UserProfile.StudentProfile): ProfileHeaderDisplayData {
        return ProfileHeaderDisplayData(
            imageUrl = profile.student.imageUrl,
            name = profile.student.name,
            infoItem1 = profile.faculty.title,
            infoItem2 = "${profile.batch.title} - ${profile.student.session}",
            bio = profile.student.bio,
        )
    }

    fun mapAcademicContents(profile: UserProfile.StudentProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Name", profile.student.name),
            ProfileInfoItem("Id", profile.student.studentId.toString()),
            ProfileInfoItem("Registration Number", profile.student.reg),
            ProfileInfoItem("Blood Group", profile.student.blood ?: "~"),
            ProfileInfoItem("Faculty", profile.faculty.title),
            ProfileInfoItem("Batch", profile.batch.name),
            ProfileInfoItem("Session", profile.student.session),
        )
    }

    fun mapConnectContents(profile: UserProfile.StudentProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Address", profile.student.address ?: "~"),
            ProfileInfoItem(
                "Phone",
                profile.student.phone ?: "~",
                if (profile.student.phone.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Call(profile.student.phone.orEmpty()),
            ),
            ProfileInfoItem(
                "Email",
                profile.student.email,
                if (profile.student.email.isEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Email(profile.student.email),
            ),
            ProfileInfoItem(
                "CV",
                profile.student.cvLink ?: "~",
                if (profile.student.cvLink.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.DownloadCv(profile.student.cvLink.orEmpty()),
            ),
            ProfileInfoItem(
                "LinkedIn",
                profile.student.linkedIn ?: "~",
                if (profile.student.linkedIn.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Link(profile.student.linkedIn.orEmpty()),
            ),
            ProfileInfoItem(
                "Facebook",
                profile.student.fbLink ?: "~",
                if (profile.student.fbLink.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Link(profile.student.fbLink.orEmpty()),
            ),
        )
    }
}