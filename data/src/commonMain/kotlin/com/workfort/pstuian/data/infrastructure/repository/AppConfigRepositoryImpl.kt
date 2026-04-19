package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.AppConfigDto
import com.workfort.pstuian.data.model.toDto
import com.workfort.pstuian.data.remote.firestore.FirestoreAppConfigDataSource
import com.workfort.pstuian.featuredomain.model.AppConfig
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import com.workfort.pstuian.util.DateTimeUtil
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppConfigRepositoryImpl(
    private val appConfigDataSource: FirestoreAppConfigDataSource,
    private val dateTimeUtil: DateTimeUtil,
    private val domainErrorMapper: DomainErrorMapper,
) : AppConfigRepository {

    override fun observeAppConfig(): Flow<AppConfig?> {
        return appConfigDataSource.observe().map {
            it?.let { (id, dto) -> dto.toAppConfig(id) }
        }
    }

    override suspend fun getAppConfig(): AppConfig? {
        return appConfigDataSource.get()?.let { (id, dto) -> dto.toAppConfig(id) }
    }

    private fun AppConfigDto.toAppConfig(id: String): AppConfig {
        val remainingMaintenance = if (!maintenance) 0.0 else {
            maintenanceUntil?.toMilliseconds()?.let {
                val now = dateTimeUtil.getTimeInMillisNow()
                if (it > now) it - now else 0.0
            } ?: 0.0
        }

        return toModel(id, remainingMaintenance)
    }

    override suspend fun updateAppConfig(config: AppConfig): DomainResult<Unit> {
        return appConfigDataSource.update(config.id, config.toDto())
            .toDomainResult(domainErrorMapper)
    }
}
