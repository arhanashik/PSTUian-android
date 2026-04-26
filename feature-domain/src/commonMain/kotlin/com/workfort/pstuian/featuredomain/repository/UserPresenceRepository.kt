package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.UserPresence
import kotlinx.coroutines.flow.Flow

interface UserPresenceRepository {
    suspend fun observeAndSyncUserPresence(userId: String)

    fun observeUserPresence(userId: String): Flow<UserPresence?>

    suspend fun removeUserPresence(userId: String)

    suspend fun getUserPresence(userId: String): UserPresence?

    fun observeActiveUsersPresence(): Flow<List<UserPresence>>
}