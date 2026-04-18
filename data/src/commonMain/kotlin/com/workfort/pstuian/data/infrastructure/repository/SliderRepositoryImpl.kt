package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.featuredomain.repository.SliderRepository

class SliderRepositoryImpl(
    private val helper: SliderApiHelper,
) : SliderRepository {
    private val sliders = mutableListOf<SliderEntity>()

    override suspend fun getSliders(forceRefresh: Boolean): List<SliderEntity> {
        if (forceRefresh || sliders.isEmpty()) {
            val newData = helper.getAll().map { it.toEntity() }
            sliders.clear()
            sliders.addAll(newData)
        }
        return sliders
    }

    override suspend fun deleteAll() {
        sliders.clear()
    }
}
