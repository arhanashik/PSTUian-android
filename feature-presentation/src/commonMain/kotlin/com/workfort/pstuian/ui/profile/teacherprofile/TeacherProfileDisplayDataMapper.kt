package com.workfort.pstuian.ui.profile.teacherprofile

import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction

class TeacherProfileDisplayDataMapper {

    fun mapHeaderData(profile: UserProfile.TeacherProfile): ProfileHeaderDisplayData {
        return ProfileHeaderDisplayData(
            imageUrl = profile.teacher.imageUrl,
            name = profile.teacher.name,
            infoItem1 = profile.faculty.title,
            infoItem2 = profile.teacher.designation,
            bio = profile.teacher.bio,
        )
    }

    fun mapAcademicContents(profile: UserProfile.TeacherProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Name", profile.teacher.name),
            ProfileInfoItem("Designation", profile.teacher.designation),
            ProfileInfoItem("Faculty", profile.faculty.title),
            ProfileInfoItem("Department", profile.teacher.department),
            ProfileInfoItem("Blood Group", profile.teacher.blood ?: "~"),
            ProfileInfoItem("Description", profile.teacher.description ?: "~"),
        )
    }

    fun mapConnectContents(profile: UserProfile.TeacherProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Address", profile.teacher.address ?: "~"),
            ProfileInfoItem(
                "Phone",
                profile.teacher.phone ?: "~",
                if (profile.teacher.phone.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Call(profile.teacher.phone.orEmpty()),
            ),
            ProfileInfoItem(
                "Email",
                profile.teacher.email.ifEmpty { "~" },
                if (profile.teacher.email.isEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Email(profile.teacher.email),
            ),
            ProfileInfoItem(
                "LinkedIn",
                profile.teacher.linkedIn ?: "~",
                if (profile.teacher.linkedIn.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Link(profile.teacher.linkedIn.orEmpty()),
            ),
            ProfileInfoItem(
                "Facebook",
                profile.teacher.fbLink ?: "~",
                if (profile.teacher.fbLink.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Link(profile.teacher.fbLink.orEmpty()),
            ),
        )
    }
}
