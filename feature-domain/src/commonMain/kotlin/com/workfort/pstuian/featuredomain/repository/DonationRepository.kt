package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DonorEntity

interface DonationRepository {
    suspend fun getDonationOption(): String

    suspend fun saveDonation(name: String, info: String, email: String, reference: String): Int

    suspend fun getDonors(): List<DonorEntity>
}