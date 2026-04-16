package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.ConfigEntity

@Entity(tableName = "config")
data class ConfigDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "android_version")
    val androidVersion: String = "",
    @ColumnInfo(name = "ios_version")
    val iosVersion: String = "",
    @ColumnInfo(name = "data_refresh_version")
    val dataRefreshVersion: String = "",
    @ColumnInfo(name = "api_version")
    val apiVersion: String = "",
    @ColumnInfo(name = "admin_api_version")
    val adminApiVersion: String = "",
    @ColumnInfo(name = "force_refresh")
    val forceRefresh: Int = 0,
    @ColumnInfo(name = "force_update")
    val forceUpdate: Int = 0,
    @ColumnInfo(name = "force_refresh_done")
    val forceRefreshDone: Boolean = false,
    @ColumnInfo(name = "force_update_done")
    val forceUpdateDone: Boolean = false,
)

fun ConfigDbEntity.toDomain() = ConfigEntity(
    id = id,
    androidVersion = androidVersion,
    iosVersion = iosVersion,
    dataRefreshVersion = dataRefreshVersion,
    apiVersion = apiVersion,
    adminApiVersion = adminApiVersion,
    forceRefresh = forceRefresh,
    forceUpdate = forceUpdate,
    forceRefreshDone = forceRefreshDone,
    forceUpdateDone = forceUpdateDone,
)

fun ConfigEntity.toDb() = ConfigDbEntity(
    id = id,
    androidVersion = androidVersion,
    iosVersion = iosVersion,
    dataRefreshVersion = dataRefreshVersion,
    apiVersion = apiVersion,
    adminApiVersion = adminApiVersion,
    forceRefresh = forceRefresh,
    forceUpdate = forceUpdate,
    forceRefreshDone = forceRefreshDone,
    forceUpdateDone = forceUpdateDone,
)
