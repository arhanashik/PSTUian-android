package com.workfort.pstuian.data.remote.firebase

import com.workfort.pstuian.data.model.UserPresenceDto
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.ServerValue
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirebaseUserPresenceDataSource(database: FirebaseDatabase) {

    private val userPresenceRef = database.reference("presence")
    private val connectedRef = database.reference(".info/connected")

    suspend fun observeAndSyncUserPresence(presenceId: String) {
        val userRef = userPresenceRef.child(presenceId)
        try {
            connectedRef.valueEvents.collectLatest { snapshot ->
                runCatching {
                    val isConnected = snapshot.value<Boolean?>() ?: false
                    if (isConnected) {
                        userRef.onDisconnect().removeValue()
                        userRef.setValue(mapOf("sessionStartedAt" to ServerValue.TIMESTAMP))
                    }
                }
            }
        } finally {
            withContext(NonCancellable) {
                runCatching { userRef.removeValue() }
            }
        }
    }

    fun observeUserPresence(presenceId: String): Flow<UserPresenceDto?> {
        return try {
            userPresenceRef.child(presenceId)
                .valueEvents
                .map { snapshot -> snapshot.value<UserPresenceDto?>() }
                .distinctUntilChanged()
        } catch (_ : Throwable) {
            emptyFlow()
        }
    }

    suspend fun removeUserPresence(presenceId: String) {
        runCatching {
            userPresenceRef.child(presenceId).removeValue()
        }
    }

    suspend fun getUserPresence(presenceId: String): UserPresenceDto? {
        val snapshot = userPresenceRef.child(presenceId)
            .valueEvents
            .filterNotNull()
            .first()

        return snapshot.value<UserPresenceDto?>()
    }

    fun observeActiveUsersPresence(): Flow<List<Pair<String, UserPresenceDto>>> {
        return try {
            userPresenceRef.valueEvents.map { snapshot ->
                snapshot.children
                    .mapNotNull { child ->
                        val presenceId = child.key
                        val presence = child.value<UserPresenceDto?>() ?: return@mapNotNull null
                        presenceId?.let { it to presence }
                    }
            }
        } catch (_ : Throwable) {
            emptyFlow()
        }
    }
}
