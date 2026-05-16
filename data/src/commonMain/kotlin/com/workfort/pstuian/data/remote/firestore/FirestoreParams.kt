package com.workfort.pstuian.data.remote.firestore


internal object FirestorePaths {

    const val APP_CONFIG_PATH = "appConfig"
    const val USERS_PATH = "users"
    const val SYSTEM_NOTIFICATIONS_PATH = "systemNotifications"
    const val USER_SYSTEM_NOTIFICATIONS_PATH = "systemNotifications"
    const val USER_CUSTOM_NOTIFICATIONS_PATH = "customNotifications"
    const val DEVICES_PATH = "devices"
}

internal object CommonFields {
    const val FIELD_STATUS = "status"
    const val FIELD_EMAIL = "email"
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_UPDATED_AT = "updatedAt"
    const val FIELD_READ_AT = "readAt"
    const val FIELD_PUBLISHED = "published"
    const val FIELD_IS_READ = "isRead"
    const val FIELD_SENT_BY = "sentBy"
    const val FIELD_USER_ID = "userId"
    const val FIELD_DEVICE_ID = "deviceId"
    const val FIELD_FCM_TOKEN = "fcmToken"
    const val FIELD_BLOCKLISTED = "blocklisted"
    const val FIELD_MODEL = "model"
    const val FIELD_PLATFORM = "platform"
    const val FIELD_DISPLAY_NAME = "displayName"
}
