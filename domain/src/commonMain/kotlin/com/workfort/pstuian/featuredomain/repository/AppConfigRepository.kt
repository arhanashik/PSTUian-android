package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.AppConfig
import com.workfort.pstuian.featuredomain.model.DomainResult
import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    fun observeAppConfig(): Flow<AppConfig?>
    suspend fun getAppConfig(): AppConfig?
    suspend fun updateAppConfig(config: AppConfig): DomainResult<Unit>
}