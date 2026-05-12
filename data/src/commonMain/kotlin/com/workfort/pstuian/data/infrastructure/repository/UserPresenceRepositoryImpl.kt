package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.firebase.FirebaseUserPresenceDataSource
import com.workfort.pstuian.featuredomain.model.UserPresence
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPresenceRepositoryImpl(
    private val userPresenceDataSource: FirebaseUserPresenceDataSource,
) : UserPresenceRepository {

    override suspend fun observeAndSyncUserPresence(presenceId: String) {
        userPresenceDataSource.observeAndSyncUserPresence(presenceId)
    }

    override fun observeUserPresence(presenceId: String): Flow<UserPresence?> {
        return userPresenceDataSource.observeUserPresence(presenceId).map { it?.toModel(presenceId) }
    }

    override suspend fun removeUserPresence(presenceId: String) {
        userPresenceDataSource.removeUserPresence(presenceId)
    }

    override suspend fun getUserPresence(presenceId: String): UserPresence? {
        return userPresenceDataSource.getUserPresence(presenceId)?.toModel(presenceId)
    }

    override fun observeActiveUsersPresence(): Flow<List<UserPresence>> {
        return userPresenceDataSource.observeActiveUsersPresence().map { list ->
            list.map { (userId, dto) -> dto.toModel(userId) }.sortedByDescending { it.sessionStartedAt }
        }
    }
}