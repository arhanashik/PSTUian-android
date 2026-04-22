package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.Slider

interface SliderRepository {
    suspend fun getSliders(forceRefresh: Boolean = false): List<Slider>

    suspend fun deleteAll()
}