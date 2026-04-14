package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.SliderEntity

interface SliderRepository {
    suspend fun getSliders(forceRefresh: Boolean = false): List<SliderEntity>

    suspend fun deleteAll()
}