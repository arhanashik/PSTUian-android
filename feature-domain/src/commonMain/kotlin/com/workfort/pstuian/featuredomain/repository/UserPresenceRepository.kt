package com.workfort.pstuian.featuredomain.repository

import kotlinx.coroutines.flow.Flow

interface UserPresenceRepository {
    suspend fun registerUserPresence(userId: String)

    suspend fun removeUserPresence(userId: String)

    suspend fun isUserOnline(userId: String): Boolean

    fun observeActiveUsers(): Flow<List<Pair<String, Long>>>
}