package com.workfort.pstuian.app.data.local.constant

import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.R

object Const {
    object RequestCode {
        const val LOCATION = 111
        const val IN_APP_UPDATE = 222
        const val PICK_IMAGE = 333
        const val PICK_PDF = 444
    }

    val backgroundList = arrayListOf(
        R.drawable.bg_gradient1,
        R.drawable.bg_gradient2,
        R.drawable.bg_gradient3,
        R.drawable.bg_gradient4,
        R.drawable.bg_gradient5,
        R.drawable.bg_gradient6,
        R.drawable.bg_gradient7,
        R.drawable.bg_gradient8,
        R.drawable.bg_gradient9,
    )

    object Key {
        const val FACULTY = "FACULTY"
        const val BATCH = "BATCH"
        const val STUDENT = "STUDENT"
        const val TEACHER = "TEACHER"
        const val EMPLOYEE = "EMPLOYEE"
        const val URL = "URL"
        const val URI = "URI"
        const val NAME = "NAME"
        const val MESSAGE = "MESSAGE"
        const val MAX_SIZE = "MAX_SIZE"
        const val PROGRESS = "PROGRESS"
        const val EDIT_ACTION = "EDIT_ACTION"
        const val EDIT_ACADEMIC = "EDIT_ACADEMIC"
        const val EDIT_CONNECT = "EDIT_CONNECT"
    }

    object Path {
        const val ROOT_PUBLIC_PATH = "/"
    }

    object Remote {
        private const val LOCAL_SERVER = "http://192.168.1.100:8888/PSTUian-web/"
        private const val LIVE_SERVER = "https://pstuian.com/"
        private val BASE_URL = if(BuildConfig.DEBUG) LOCAL_SERVER else LIVE_SERVER
        val TERMS_AND_CONDITIONS = "${BASE_URL}terms_and_conditions.php"
        val PRIVACY_POLICY = "${BASE_URL}privacy_policy.php"

        private const val LOCAL_API_SERVER = "${LOCAL_SERVER}api/mobile/v1/"
        private const val DEV_API_SERVER = "https://api-dev.pstuian.com/mobile/v1/"
        private const val LIVE_API_SERVER = "https://api.pstuian.com/mobile/v1/"
        val BASE_API_URL = if(BuildConfig.DEBUG) LOCAL_API_SERVER else LIVE_API_SERVER
    }
}