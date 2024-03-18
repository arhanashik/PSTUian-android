package com.workfort.pstuian.appconstant

object Const {
    object RequestCode {
        const val LOCATION = 111
        const val IN_APP_UPDATE = 222
        const val PICK_IMAGE = 333
        const val PICK_PDF = 444
    }

    object Key {
        const val FACULTY = "FACULTY"
        const val BATCH = "BATCH"
        const val STUDENT = "STUDENT"
        const val STUDENT_ID = "STUDENT_ID"
        const val TEACHER = "TEACHER"
        const val TEACHER_ID = "TEACHER_ID"
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
        const val USER_TYPE = "USER_TYPE"
        const val EXTRA_IMAGE_TRANSITION_NAME = "EXTRA_IMAGE_TRANSITION_NAME"
        const val EXTRA_ITEM = "EXTRA_ITEM"
        const val EXTRA_IS_UPDATED = "EXTRA_IS_UPDATED"
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
        const val NOTIFICATION = "${BuildConfig.APPLICATION_ID}.NOTIFICATION"
    }

    object NotificationType {
        const val DEFAULT = "default"
        const val BLOOD_DONATION = "blood_donation"
        const val NEWS = "news"
        const val HELP = "help"
    }
}