package com.workfort.pstuian.app.data.local.blooddonation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 17:13.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/10.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

@Parcelize
data class BloodDonation(
    val id: Int,
    @SerializedName("request_id")
    val requestId: Int?,
    @SerializedName("date")
    val date: String,
    @SerializedName("info")
    val info: String?,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val image_url: String,
) : Parcelable
