package com.workfort.pstuian.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.appconstant.NetworkConst
import kotlinx.parcelize.Parcelize

/**
 *  ****************************************************************************
 *  * Created by : arhan on 28 Oct, 2021 at 13:55.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

@Parcelize
data class DeviceEntity(
    var id: String,
    @SerializedName(NetworkConst.Params.FCM_TOKEN)
    var fcmToken: String,
    var model: String? = "",
    @SerializedName(NetworkConst.Params.ANDROID_VERSION)
    var androidVersion: String? = "",
    @SerializedName(NetworkConst.Params.APP_VERSION_CODE)
    var appVersionCode: Int? = 0,
    @SerializedName(NetworkConst.Params.APP_VERSION_NAME)
    var appVersionName: String? = "",
    @SerializedName(NetworkConst.Params.IP_ADDRESS)
    var ipAddress: String? = "",
    var lat: String? = "",
    var lng: String? = "",
    var locale: String? = "",
    @SerializedName(NetworkConst.Params.CREATED_AT)
    var createdAt: String? = "",
    @SerializedName(NetworkConst.Params.UPDATED_AT)
    var updatedAt: String? = "",
) : Parcelable {
    override fun equals(other: Any?) = (other is DeviceEntity)
            && other.id == id
            && other.fcmToken == fcmToken
            && other.model == model
            && other.androidVersion == androidVersion
            && other.appVersionCode == appVersionCode
            && other.appVersionName == appVersionName
            && other.ipAddress == ipAddress
            && other.lat == lat
            && other.lng == lng
            && other.locale == locale
            && other.createdAt == createdAt
            && other.updatedAt == updatedAt

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fcmToken.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + androidVersion.hashCode()
        result = 31 * result + appVersionCode.hashCode()
        result = 31 * result + appVersionName.hashCode()
        result = 31 * result + ipAddress.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + locale.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }
}