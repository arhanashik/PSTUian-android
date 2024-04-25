package com.workfort.pstuian.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
    val imageUrl: String?,
) : Parcelable
