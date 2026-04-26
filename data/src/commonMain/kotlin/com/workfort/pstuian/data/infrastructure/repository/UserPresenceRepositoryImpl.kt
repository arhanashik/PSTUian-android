package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.firebase.FirebaseUserPresenceDataSource
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import kotlinx.coroutines.flow.Flow

class UserPresenceRepositoryImpl(
    private val userPresenceDataSource: FirebaseUserPresenceDataSource,
) : UserPresenceRepository {

    override suspend fun registerUserPresence(userId: String) {
        userPresenceDataSource.registerUserPresence(userId)
    }

    override suspend fun removeUserPresence(userId: String) {
        userPresenceDataSource.removeUserPresence(userId)
    }

    override suspend fun isUserOnline(userId: String): Boolean {
        return userPresenceDataSource.isUserOnline(userId)
    }

    override fun observeActiveUsers(): Flow<List<Pair<String, Long>>> {
        return userPresenceDataSource.observeActiveUsers()
    }
}