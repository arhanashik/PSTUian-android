package com.workfort.pstuian.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import com.workfort.pstuian.ui.common.icons.AppIcons
import org.jetbrains.compose.resources.StringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_logo
import pstuian.feature_presentation.generated.resources.img_admission_support
import pstuian.feature_presentation.generated.resources.img_donors
import pstuian.feature_presentation.generated.resources.img_help
import pstuian.feature_presentation.generated.resources.label_admission_support
import pstuian.feature_presentation.generated.resources.label_clear_data
import pstuian.feature_presentation.generated.resources.label_donation_list
import pstuian.feature_presentation.generated.resources.label_university_website
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_donate
import pstuian.feature_presentation.generated.resources.txt_need_blood
import pstuian.feature_presentation.generated.resources.txt_need_help
import pstuian.feature_presentation.generated.resources.txt_rate_app
import pstuian.feature_presentation.generated.resources.txt_settings

sealed interface Action {
    data object AdmissionSupport: Action
    data object Donors: Action
    data object VarsityWebsite: Action
    data object ContactUs: Action
    data object RequestBloodDonation: Action
    data object CheckIn: Action
    data object RateApp: Action
    data object ClearData: Action
    data object Settings: Action
    data object Donate: Action
}

data class ActionItem(
    val title: StringResource,
    val icon: Any,
    val action: Action,
)

val informationItems = listOf(
    ActionItem(
        Res.string.label_university_website,
        Res.drawable.ic_logo,
        action = Action.VarsityWebsite,
    ),
    ActionItem(
        Res.string.label_admission_support,
        Res.drawable.img_admission_support,
        action = Action.AdmissionSupport,
    ),
    ActionItem(
        Res.string.label_donation_list,
        Res.drawable.img_donors,
        action = Action.Donors,
    ),
    ActionItem(
        Res.string.txt_need_help,
        Res.drawable.img_help,
        action = Action.ContactUs,
    ),
)

val optionsItems = listOf(
    ActionItem(
        Res.string.txt_need_blood,
        AppIcons.BloodDrop,
        action = Action.RequestBloodDonation,
    ),
    ActionItem(
        Res.string.txt_check_in,
        AppIcons.CheckIn,
        action = Action.CheckIn,
    ),
    ActionItem(
        Res.string.txt_settings,
        Icons.Default.Settings,
        action = Action.Settings,
    ),
    ActionItem(
        Res.string.txt_rate_app,
        Icons.Default.Star,
        action = Action.RateApp,
    ),
    ActionItem(
        Res.string.label_clear_data,
        Icons.Outlined.Delete,
        action = Action.ClearData,
    ),
    ActionItem(
        Res.string.txt_donate,
        Icons.Default.Favorite,
        action = Action.Donate,
    ),
)
