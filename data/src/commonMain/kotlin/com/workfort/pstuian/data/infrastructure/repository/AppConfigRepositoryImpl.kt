package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.toDto
import com.workfort.pstuian.data.remote.firestore.FirestoreAppConfigDataSource
import com.workfort.pstuian.featuredomain.model.AppConfig
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppConfigRepositoryImpl(
    private val appConfigDataSource: FirestoreAppConfigDataSource,
    private val domainErrorMapper: DomainErrorMapper,
) : AppConfigRepository {

    override fun observeAppConfig(): Flow<AppConfig?> {
        return appConfigDataSource.observe().map {
            it?.let { (id, dto) -> dto.toModel(id) }
        }
    }

    override suspend fun getAppConfig(): AppConfig? {
        return appConfigDataSource.get()?.let { (id, dto) -> dto.toModel(id) }
    }

    override suspend fun updateAppConfig(config: AppConfig): DomainResult<Unit> {
        return appConfigDataSource.update(config.id, config.toDto())
            .toDomainResult(domainErrorMapper)
    }
}
