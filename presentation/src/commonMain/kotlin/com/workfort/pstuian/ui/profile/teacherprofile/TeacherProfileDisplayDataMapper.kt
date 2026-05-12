package com.workfort.pstuian.ui.profile.teacherprofile

import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction

class TeacherProfileDisplayDataMapper {

    fun mapHeaderData(profile: UserProfile.TeacherProfile): ProfileHeaderDisplayData {
        return ProfileHeaderDisplayData(
            imageUrl = profile.user.imageUrl,
            name = profile.user.name,
            infoItem1 = profile.faculty.title,
            infoItem2 = profile.user.designation,
            bio = profile.user.bio,
        )
    }

    fun mapAcademicContents(profile: UserProfile.TeacherProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Name", profile.user.name),
            ProfileInfoItem("Designation", profile.user.designation),
            ProfileInfoItem("Faculty", profile.faculty.title),
            ProfileInfoItem("Department", profile.user.department),
            ProfileInfoItem("Blood Group", profile.user.blood ?: "~"),
            ProfileInfoItem("Description", profile.user.description ?: "~"),
        )
    }

    fun mapConnectContents(profile: UserProfile.TeacherProfile): List<ProfileInfoItem> {
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
                profile.user.email.ifEmpty { "~" },
                if (profile.user.email.isEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Email(profile.user.email),
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
