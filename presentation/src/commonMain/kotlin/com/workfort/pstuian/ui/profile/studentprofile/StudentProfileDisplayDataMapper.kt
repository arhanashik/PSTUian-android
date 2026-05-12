package com.workfort.pstuian.ui.profile.studentprofile

import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction


class StudentProfileDisplayDataMapper {

    fun mapHeaderData(profile: UserProfile.StudentProfile): ProfileHeaderDisplayData {
        return ProfileHeaderDisplayData(
            imageUrl = profile.user.imageUrl,
            name = profile.user.name,
            infoItem1 = profile.faculty.title,
            infoItem2 = "${profile.batch.title} - ${profile.user.session}",
            bio = profile.user.bio,
        )
    }

    fun mapAcademicContents(profile: UserProfile.StudentProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Name", profile.user.name),
            ProfileInfoItem("Id", profile.user.userId.toString()),
            ProfileInfoItem("Registration Number", profile.user.reg),
            ProfileInfoItem("Blood Group", profile.user.blood ?: "~"),
            ProfileInfoItem("Faculty", profile.faculty.title),
            ProfileInfoItem("Batch", profile.batch.name),
            ProfileInfoItem("Session", profile.user.session),
        )
    }

    fun mapConnectContents(profile: UserProfile.StudentProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Address", profile.user.address ?: "~"),
            ProfileInfoItem(
                "Phone",
                profile.user.phone ?: "~",
                if (profile.user.phone.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Call(profile.user.phone.orEmpty()),
            ),
            ProfileInfoItem(
                "Email",
                profile.user.email,
                if (profile.user.email.isEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Email(profile.user.email),
            ),
            ProfileInfoItem(
                "CV",
                profile.user.cvLink ?: "~",
                if (profile.user.cvLink.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.DownloadCv(profile.user.cvLink.orEmpty()),
            ),
            ProfileInfoItem(
                "LinkedIn",
                profile.user.linkedIn ?: "~",
                if (profile.user.linkedIn.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Link(profile.user.linkedIn.orEmpty()),
            ),
            ProfileInfoItem(
                "Facebook",
                profile.user.fbLink ?: "~",
                if (profile.user.fbLink.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Link(profile.user.fbLink.orEmpty()),
            ),
        )
    }
}