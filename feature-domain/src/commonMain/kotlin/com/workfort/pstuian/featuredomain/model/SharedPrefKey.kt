package com.workfort.pstuian.featuredomain.model

data class SharedPrefKey(
    val name: String,
    val clearOnSignOut: Boolean
) {
    companion object {
        private val allKeys = mutableListOf<SharedPrefKey>()

        fun getAllKeys(): List<SharedPrefKey> = allKeys

        // don't clear on sign out keys
        val SHOW_NOTIFICATION = create("show_notification", false)
        val APP_THEME = create("app_theme", false)
        val FCM_TOKEN = create("fcm_token", false)
        val DEVICE_ID = create("device_id", false)

        // clear on sign out keys
        val AUTH_TOKEN = create("auth_token", true)
        val FIRESTORE_CACHE_CLEARED = create("firestore_cache_cleared", true)
        val DONATION_ID = create("donation_id", true)

        fun create(name: String, clearOnSignOut: Boolean): SharedPrefKey {
            return SharedPrefKey(name, clearOnSignOut).also { allKeys.add(it) }
        }
    }
}
