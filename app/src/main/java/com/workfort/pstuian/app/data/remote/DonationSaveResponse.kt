package com.workfort.pstuian.app.data.remote

import com.google.gson.annotations.SerializedName

data class DonationSaveResponse (val success: Boolean,
                                 val message: String,
                                 @SerializedName("donation_id") val donationId: String)