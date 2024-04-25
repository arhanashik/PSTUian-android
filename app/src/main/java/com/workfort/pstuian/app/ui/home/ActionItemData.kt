package com.workfort.pstuian.app.ui.home

import android.content.Context
import androidx.annotation.DrawableRes
import com.workfort.pstuian.R


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
    val title: String,
    @DrawableRes val icon: Int,
    val action: Action,
)

fun getInformationItems(context: Context) = listOf(
    ActionItem(
        context.getString(R.string.label_admission_support),
        R.drawable.img_admission_support,
        action = Action.AdmissionSupport,
    ),
    ActionItem(
        context.getString(R.string.label_donation_list),
        R.drawable.img_donors,
        action = Action.Donors,
    ),
    ActionItem(
        context.getString(R.string.label_university_website),
        R.drawable.img_pstu_website,
        action = Action.VarsityWebsite,
    ),
    ActionItem(
        context.getString(R.string.txt_need_help),
        R.drawable.img_help,
        action = Action.ContactUs,
    ),
)

fun getOptionsItems(context: Context) = listOf(
    ActionItem(
        context.getString(R.string.txt_need_blood),
        R.drawable.ic_blood_drop,
        action = Action.RequestBloodDonation,
    ),
    ActionItem(
        context.getString(R.string.txt_check_in),
        R.drawable.ic_check_in,
        action = Action.CheckIn,
    ),
    ActionItem(
        context.getString(R.string.txt_rate_app),
        R.drawable.ic_star_face,
        action = Action.RateApp,
    ),
    ActionItem(
        context.getString(R.string.label_clear_data),
        R.drawable.ic_database_remove_outline,
        action = Action.ClearData,
    ),
    ActionItem(
        context.getString(R.string.txt_settings),
        R.drawable.ic_settings,
        action = Action.Settings,
    ),
    ActionItem(
        context.getString(R.string.txt_donate),
        R.drawable.ic_hand_heart,
        action = Action.Donate,
    ),
)