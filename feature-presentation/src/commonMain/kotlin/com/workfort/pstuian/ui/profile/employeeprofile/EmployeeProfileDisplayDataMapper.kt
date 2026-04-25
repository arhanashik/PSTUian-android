package com.workfort.pstuian.ui.profile.employeeprofile

import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction

class EmployeeProfileDisplayDataMapper {

    fun mapHeaderData(profile: EmployeeProfile): ProfileHeaderDisplayData {
        return ProfileHeaderDisplayData(
            imageUrl = profile.employee.imageUrl,
            name = profile.employee.name,
            infoItem1 = profile.faculty.title,
            infoItem2 = profile.employee.designation,
            bio = profile.employee.bio,
        )
    }

    fun mapAcademicContents(profile: EmployeeProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Name", profile.employee.name),
            ProfileInfoItem("Designation", profile.employee.designation),
            ProfileInfoItem("Faculty", profile.faculty.title),
            ProfileInfoItem("Department", profile.employee.department ?: "~"),
            ProfileInfoItem("Blood Group", profile.employee.blood ?: "~"),
        )
    }

    fun mapConnectContents(profile: EmployeeProfile): List<ProfileInfoItem> {
        return listOf(
            ProfileInfoItem("Address", profile.employee.address ?: "~"),
            ProfileInfoItem(
                "Phone",
                profile.employee.phone ?: "~",
                if (profile.employee.phone.isNullOrEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Call(profile.employee.phone.orEmpty()),
            ),
            ProfileInfoItem(
                "Email",
                profile.employee.email.ifEmpty { "~" },
                if (profile.employee.email.isEmpty()) ProfileInfoItemAction.None
                else ProfileInfoItemAction.Email(profile.employee.email),
            ),
        )
    }
}
