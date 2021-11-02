package com.workfort.pstuian.app.data.local.pref

interface PrefProp {
    val prefName: String
        get() = "com.workfort.pstuian"

    val keyDeviceId: String
        get() = "device_id"

    val keyFcmToken: String
        get() = "fcm_token"

    val keyFirstRun: String
        get() = "first_run"

    val keyAuthToken: String
        get() = "auth_token"

    val keyDonateOption: String
        get() = "donation_option"

    val keyDonationId: String
        get() = "donation_id"

    val keyHasNewNotification: String
        get() = "has_new_notification"
}