package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.featuredomain.model.Slider
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.SliderRepository
import io.github.aakira.napier.Napier

class SliderRepositoryImpl(
    private val helper: SliderApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : SliderRepository {
    private val cache = mutableListOf<Slider>()

    override suspend fun getSliders(forceRefresh: Boolean): List<Slider> {
        if (forceRefresh || cache.isEmpty()) {
            helper.getAll().toDomainResult(domainErrorMapper).map { dtos ->
                val newData = dtos.map { it.toModel() }
                cache.clear()
                cache.addAll(newData)
                Napier.e("testR data")
                Napier.e("testR $newData")
            }
        }
        return cache
    }

    override suspend fun deleteAll() {
        cache.clear()
    }
}
