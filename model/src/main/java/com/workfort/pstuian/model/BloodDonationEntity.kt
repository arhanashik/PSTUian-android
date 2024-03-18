package com.workfort.pstuian.model

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
 *  ****************************************************************************
 */

@Parcelize
data class BloodDonationEntity(
    val id: Int,
    @SerializedName("request_id")
    var requestId: Int?,
    @SerializedName("date")
    var date: String,
    @SerializedName("info")
    var info: String?,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_type")
    val userType: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image_url")
    val image_url: String?,
) : Parcelable
