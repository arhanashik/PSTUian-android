package com.workfort.pstuian.featuredomain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.appconstant.ColumnNames

@Entity(tableName = TableNames.CONFIG)
data class ConfigEntity(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = ColumnNames.Config.ANDROID_VERSION)
    var androidVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.IOS_VERSION)
    var iosVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.DATA_REFRESH_VERSION)
    var dataRefreshVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.API_VERSION)
    var apiVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.ADMIN_API_VERSION)
    var adminApiVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.FORCE_REFRESH)
    var forceRefresh: Int = 0,
    @ColumnInfo(name = ColumnNames.Config.FORCE_UPDATE)
    var forceUpdate: Int = 0,
    @ColumnInfo(name = ColumnNames.Config.FORCE_REFRESH_DONE)
    var forceRefreshDone: Boolean = false,
    @ColumnInfo(name = ColumnNames.Config.FORCE_UPDATE_DONE)
    var forceUpdateDone: Boolean = false,
)
