package com.workfort.pstuian.app.data.remote

import com.google.gson.annotations.SerializedName

data class DonationOptionResponse (val success: Boolean,
                                   val message: String,
                                   @SerializedName("donation_option")
                                   val donationOption: String)