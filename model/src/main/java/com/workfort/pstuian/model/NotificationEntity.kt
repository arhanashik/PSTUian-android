package com.workfort.pstuian.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.appconstant.NetworkConst
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationEntity(
    var id: Int,
    var type: String,
    var title: String? = "",
    var message: String,
    @SerializedName(NetworkConst.Params.CREATED_AT)
    var date: String? = "",
) : Parcelable {
    override fun equals(other: Any?) = (other is NotificationEntity)
            && other.id == id
            && other.type == type
            && other.title == title
            && other.message == message
            && other.date == date

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}