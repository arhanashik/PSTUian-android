package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.firebase.FirebaseUserPresenceDataSource
import com.workfort.pstuian.featuredomain.model.UserPresence
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPresenceRepositoryImpl(
    private val userPresenceDataSource: FirebaseUserPresenceDataSource,
) : UserPresenceRepository {

    override suspend fun observeAndSyncUserPresence(userId: String) {
        userPresenceDataSource.observeAndSyncUserPresence(userId)
    }

    override fun observeUserPresence(userId: String): Flow<UserPresence?> {
        return userPresenceDataSource.observeUserPresence(userId).map { it?.toModel(userId) }
    }

    override suspend fun removeUserPresence(userId: String) {
        userPresenceDataSource.removeUserPresence(userId)
    }

    override suspend fun getUserPresence(userId: String): UserPresence? {
        return userPresenceDataSource.getUserPresence(userId)?.toModel(userId)
    }

    override fun observeActiveUsersPresence(): Flow<List<UserPresence>> {
        return userPresenceDataSource.observeActiveUsersPresence().map { list ->
            list.map { (userId, dto) -> dto.toModel(userId) }.sortedByDescending { it.lastSeenAt }
        }
    }
}