package com.workfort.pstuian.app.data.local.pref

interface PrefProp {
    val prefName: String
        get() = "com.workfort.pstuian"

    val keyFirstRun: String
        get() = "first_run"

    val keyDonateOption: String
        get() = "donation_option"

    val keyDonationId: String
        get() = "donation_id"
}