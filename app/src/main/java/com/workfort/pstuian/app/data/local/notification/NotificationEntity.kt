package com.workfort.pstuian.app.data.local.notification

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.constant.Const
import kotlinx.parcelize.Parcelize

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 20:39.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

@Parcelize
data class NotificationEntity(
    var id: Int,
    var type: String,
    var title: String? = "",
    var message: String,
    @SerializedName(Const.Params.CREATED_AT)
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