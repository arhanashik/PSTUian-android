package com.workfort.pstuian.app.data.local.database

object ColumnNames {
    const val SL = "sl"

    object Config {
        const val ID = "id"
        const val ANDROID_VERSION = "android_version"
        const val IOS_VERSION = "ios_version"
        const val DATA_REFRESH_VERSION = "data_refresh_version"
        const val API_VERSION = "api_version"
        const val ADMIN_API_VERSION = "admin_api_version"
        const val FORCE_REFRESH = "force_refresh"
        const val FORCE_UPDATE = "force_update"
        const val FORCE_REFRESH_DONE = "force_refresh_done"
        const val FORCE_UPDATE_DONE = "force_update_done"
    }

    object Slider {
        const val ID = "id"
        const val TITLE = "title"
        const val IMAGE_URL = "image_url"
    }

    object Faculty {
        const val ID = "id"
        const val SHORT_TITLE = "short_title"
        const val TITLE = "title"
    }

    object Student {
        const val NAME = "name"
        const val ID = "id"
        const val REG = "reg"
        const val PHONE = "phone"
        const val LINKED_IN = "linked_in"
        const val FB_LINK = "fb_link"
        const val BLOOD = "blood"
        const val ADDRESS = "address"
        const val EMAIL = "email"
        const val BATCH_ID = "batch_id"
        const val SESSION = "session"
        const val FACULTY_ID = "faculty_id"
        const val IMAGE_URL = "image_url"
        const val CV_LINK = "cv_link"
        const val BIO = "bio"
    }

    object Teacher {
        const val ID = "id"
        const val NAME = "name"
        const val DESIGNATION = "designation"
        const val BIO = "bio"
        const val PHONE = "phone"
        const val LINKED_IN = "linked_in"
        const val FB_LINK = "fb_link"
        const val ADDRESS = "address"
        const val EMAIL = "email"
        const val DEPARTMENT = "department"
        const val BLOOD = "blood"
        const val FACULTY_ID = "faculty_id"
        const val IMAGE_URL = "image_url"
    }

    object Employee {
        const val ID = "id"
        const val NAME = "name"
        const val DESIGNATION = "designation"
        const val DEPARTMENT = "department"
        const val PHONE = "phone"
        const val ADDRESS = "address"
        const val FACULTY_ID = "faculty_id"
        const val IMAGE_URL = "image_url"
    }

    object Batch {
        const val ID = "id"
        const val NAME = "name"
        const val TITLE = "title"
        const val SESSION = "session"
        const val FACULTY_ID = "faculty_id"
        const val TOTAL_STUDENT = "total_student"
    }

    object Course {
        const val ID = "id"
        const val COURSE_CODE = "course_code"
        const val COURSE_TITLE = "course_title"
        const val CREDIT_HOUR = "credit_hour"
        const val FACULTY_ID = "faculty_id"
        const val STATUS = "status"
    }
}