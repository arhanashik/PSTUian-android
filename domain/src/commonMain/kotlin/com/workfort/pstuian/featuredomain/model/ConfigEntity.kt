package com.workfort.pstuian.featuredomain.model

data class ConfigEntity(
    var id: Int = 0,
    var androidVersion: String = "",
    var iosVersion: String = "",
    var dataRefreshVersion: String = "",
    var apiVersion: String = "",
    var adminApiVersion: String = "",
    var forceRefresh: Int = 0,
    var forceUpdate: Int = 0,
    var forceRefreshDone: Boolean = false,
    var forceUpdateDone: Boolean = false,
)
