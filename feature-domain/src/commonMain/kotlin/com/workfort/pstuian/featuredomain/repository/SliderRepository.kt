package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Slider

interface SliderRepository {
    suspend fun getSliders(forceRefresh: Boolean = false): DomainResult<List<Slider>>
}