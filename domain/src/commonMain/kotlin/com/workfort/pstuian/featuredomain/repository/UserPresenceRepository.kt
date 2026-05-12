package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.UserPresence
import kotlinx.coroutines.flow.Flow

interface UserPresenceRepository {
    suspend fun observeAndSyncUserPresence(presenceId: String)

    fun observeUserPresence(presenceId: String): Flow<UserPresence?>

    suspend fun removeUserPresence(presenceId: String)

    suspend fun getUserPresence(presenceId: String): UserPresence?

    fun observeActiveUsersPresence(): Flow<List<UserPresence>>
}