package com.workfort.pstuian.ui.profile.employeeprofile

import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction

class EmployeeProfileDisplayDataMapper {

    fun mapHeaderData(profile: UserProfile.EmployeeProfile): ProfileHeaderDisplayData {
        return ProfileHeaderDisplayData(
            imageUrl = profile.user.imageUrl,
            name = profile.user.name,
            infoItem1 = profile.faculty.title,
            infoItem2 = profile.user.designation,
            bio = profile.user.bio,
        )
    }

    fun mapAcademicContents(profile: UserProfile.EmployeeProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Name", profile.user.name),
            ProfileInfoItem("Designation", profile.user.designation),
            ProfileInfoItem("Faculty", profile.faculty.title),
            ProfileInfoItem("Department", profile.user.department ?: "~"),
            ProfileInfoItem("Blood Group", profile.user.blood ?: "~"),
        )
    }

    fun mapConnectContents(profile: UserProfile.EmployeeProfile): List<ProfileInfoItem> {
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
        )
    }
}
