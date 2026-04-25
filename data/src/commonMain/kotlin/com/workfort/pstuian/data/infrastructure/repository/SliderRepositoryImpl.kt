package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Slider
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.SliderRepository

class SliderRepositoryImpl(
    private val helper: SliderApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : SliderRepository {
    private val cache = mutableSetOf<Slider>()

    override suspend fun getSliders(forceRefresh: Boolean): DomainResult<List<Slider>> {
        if (forceRefresh) cache.clear()

        if (cache.isNotEmpty()) return DomainResult.success(cache.toList())

        return helper.getAll()
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess {
                cache.addAll(it)
            }
    }
}
