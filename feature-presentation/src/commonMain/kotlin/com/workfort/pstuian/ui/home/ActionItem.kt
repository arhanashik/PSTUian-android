package com.workfort.pstuian.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.SupportAgent
import org.jetbrains.compose.resources.StringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_admission_support
import pstuian.feature_presentation.generated.resources.ic_blood_donation
import pstuian.feature_presentation.generated.resources.ic_check_in
import pstuian.feature_presentation.generated.resources.ic_logo
import pstuian.feature_presentation.generated.resources.img_admission_support
import pstuian.feature_presentation.generated.resources.label_admission_support
import pstuian.feature_presentation.generated.resources.label_blood_donation
import pstuian.feature_presentation.generated.resources.label_support
import pstuian.feature_presentation.generated.resources.label_university_website
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_donate
import pstuian.feature_presentation.generated.resources.txt_settings

sealed interface Action {
    data object UniversityWebsite: Action
    data object AdmissionSupport: Action
    data object BloodDonation: Action
    data object CheckIn: Action
    data object Settings: Action
    data object Support: Action
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
        action = Action.UniversityWebsite,
    ),
    ActionItem(
        Res.string.label_admission_support,
        Res.drawable.ic_admission_support,
        action = Action.AdmissionSupport,
    ),
    ActionItem(
        Res.string.label_blood_donation,
        Res.drawable.ic_blood_donation,
        action = Action.BloodDonation,
    ),
    ActionItem(
        Res.string.txt_check_in,
        Res.drawable.ic_check_in,
        action = Action.CheckIn,
    ),
)

val optionsItems = listOf(
    ActionItem(
        Res.string.txt_settings,
        Icons.Default.Settings,
        action = Action.Settings,
    ),
    ActionItem(
        Res.string.label_support,
        Icons.Default.SupportAgent,
        action = Action.Support,
    ),
    ActionItem(
        Res.string.txt_donate,
        Icons.Default.Favorite,
        action = Action.Donate,
    ),
)
