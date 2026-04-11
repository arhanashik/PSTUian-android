package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.model.dto.DonorDto


interface DonationApiHelper {
    suspend fun getDonationOption(): String
    suspend fun saveDonation(name: String, info: String, email: String, reference: String): Int
    suspend fun getDonors(): List<DonorDto>
}