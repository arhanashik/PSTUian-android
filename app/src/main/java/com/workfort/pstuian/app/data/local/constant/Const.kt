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
        const val UPDATED = "UPDATED"
    }

    object Params {
        const val ID = "id"
        const val USER_ID = "user_id"

        const val DEVICE_ID = "device_id"
        const val FCM_TOKEN = "fcm_token"
        const val MODEL = "model"
        const val ANDROID_VERSION = "android_version"
        const val APP_VERSION_CODE = "app_version_code"
        const val APP_VERSION_NAME = "app_version_name"
        const val IP_ADDRESS = "ip_address"
        const val LAT = "lat"
        const val LNG = "lng"
        const val LOCALE = "locale"

        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val USER_TYPE = "user_type"

        const val NAME = "name"
        const val REG = "reg"
        const val FACULTY_ID = "faculty_id"
        const val BATCH_ID = "batch_id"
        const val SESSION = "session"

        const val CREATED_AT = "created_at"
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

        private const val CONFIG_API_PATH = "config.php?call="
        private const val DEVICE_API_PATH = "device.php?call="
        private const val AUTH_API_PATH = "auth.php?call="
        private const val NOTIFICATION_API_PATH = "notification.php?call="
        object Api {
            const val GET_CONFIG =  "${CONFIG_API_PATH}getLatest"
            const val REGISTER_DEVICE = "${DEVICE_API_PATH}register"
            const val UPDATE_FCM_TOKEN = "${DEVICE_API_PATH}updateFcmToken"

            object Auth {
                const val SIGN_IN = "${AUTH_API_PATH}signIn"
                const val SIGN_UP = "${AUTH_API_PATH}signUp"
                const val SIGN_OUT = "${AUTH_API_PATH}signOut"
            }

            object Notification {
                const val GET_ALL = "${NOTIFICATION_API_PATH}getAll"
            }
        }
    }

    object Fcm {
        object DataKey {
            const val TYPE ="type"
            const val TITLE ="title"
            const val MESSAGE ="message"
            const val ACTION ="action"
        }
    }

    object IntentAction {
        private const val APP_ID = BuildConfig.APPLICATION_ID
        const val NOTIFICATION = "${APP_ID}.NOTIFICATION"
    }

    object NotificationType {
        const val DEFAULT = "default"
        const val BLOOD_DONATION = "blood_donation"
        const val NEWS = "news"
        const val HELP = "help"
    }
}