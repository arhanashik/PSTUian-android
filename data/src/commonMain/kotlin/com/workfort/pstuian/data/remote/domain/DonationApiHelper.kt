package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.DonorDto


interface DonationApiHelper {
    suspend fun getDonationOption(): String
    suspend fun saveDonation(name: String, info: String, email: String, reference: String): Int
    suspend fun getDonors(): List<DonorDto>
}