package com.workfort.pstuian.common.navigation

import com.workfort.pstuian.featuredomain.model.UserType

sealed class AppScreen {
    object Splash : AppScreen()
    object SignIn : AppScreen()
    object Home : AppScreen()
    data class Students(val batchId: Int) : AppScreen()
    data class Teachers(val userId: Int) : AppScreen()
    data class Employees(val userId: Int) : AppScreen()
    object BloodDonationRequestList : AppScreen()
    object BloodDonationRequestCreate : AppScreen()
    data class Profile(val userId: Int, val userType: UserType) : AppScreen()
    object LocationPicker : AppScreen()
    object Donate : AppScreen()
}