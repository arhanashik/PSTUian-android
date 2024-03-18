package com.workfort.pstuian.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.CONFIG)
data class ConfigEntity(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = ColumnNames.Config.ANDROID_VERSION)
    @SerializedName("android_version")
    var androidVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.IOS_VERSION)
    @SerializedName("ios_version")
    var iosVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.DATA_REFRESH_VERSION)
    @SerializedName("data_refresh_version")
    var dataRefreshVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.API_VERSION)
    @SerializedName("api_version")
    var apiVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.ADMIN_API_VERSION)
    @SerializedName("admin_api_version")
    var adminApiVersion: String = "",
    @ColumnInfo(name = ColumnNames.Config.FORCE_REFRESH)
    @SerializedName("force_refresh")
    var forceRefresh: Int = 0,
    @ColumnInfo(name = ColumnNames.Config.FORCE_UPDATE)
    @SerializedName("force_update")
    var forceUpdate: Int = 0,
    // next two column is to store the force refresh/update state locally
    @ColumnInfo(name = ColumnNames.Config.FORCE_REFRESH_DONE)
    var forceRefreshDone: Boolean = false,
    @ColumnInfo(name = ColumnNames.Config.FORCE_UPDATE_DONE)
    var forceUpdateDone: Boolean = false,
) : Parcelable {
    override fun equals(other: Any?) = (other is ConfigEntity)
            && other.id == id
            && other.androidVersion == androidVersion
            && other.iosVersion == iosVersion
            && other.dataRefreshVersion == dataRefreshVersion
            && other.apiVersion == apiVersion
            && other.adminApiVersion == adminApiVersion
            && other.forceRefresh == forceRefresh
            && other.forceUpdate == forceUpdate

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + androidVersion.hashCode()
        result = 31 * result + iosVersion.hashCode()
        result = 31 * result + dataRefreshVersion.hashCode()
        result = 31 * result + apiVersion.hashCode()
        result = 31 * result + adminApiVersion.hashCode()
        result = 31 * result + forceRefresh
        result = 31 * result + forceUpdate
        return result
    }
}
