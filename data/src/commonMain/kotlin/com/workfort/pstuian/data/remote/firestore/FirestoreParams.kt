package com.workfort.pstuian.data.remote.firestore


internal object FirestorePaths {

    const val APP_CONFIG_PATH = "appConfig"
    const val COLLECTION_PATH = "collection"
    const val COLLECTION_USER_STATS_PATH = "userStats"
    const val COLLECTION_BILLS_PATH = "bills"
    const val COLLECTION_SETTLEMENTS_PATH = "settlements"
    const val USERS_PATH = "users"
    const val NOTIFICATIONS_PATH = "notifications"
    const val USER_SYSTEM_NOTIFICATIONS_PATH = "systemNotifications"
    const val USER_CUSTOM_NOTIFICATIONS_PATH = "customNotifications"
    const val DEVICES_PATH = "devices"
}

internal object CommonFields {
    const val FIELD_OWNER_ID = "ownerId"
    const val FIELD_SHARED_OWNER_IDS = "sharedOwnerIds"
    const val FIELD_STATUS = "status"
    const val FIELD_EMAIL = "email"
    const val FIELD_BILL_COUNT = "billCount"
    const val FIELD_SETTLEMENT_COUNT = "settlementCount"
    const val FIELD_TOTAL_AMOUNT = "totalAmount"
    const val FIELD_TOTAL_SETTLEMENT = "totalSettlement"
    const val FIELD_CREATED_AT = "createdAt"
    const val FIELD_UPDATED_AT = "updatedAt"
    const val FIELD_READ_AT = "readAt"
    const val FIELD_IS_PUBLISHED = "isPublished"
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
