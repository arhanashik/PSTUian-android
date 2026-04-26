package com.workfort.pstuian.data.remote.firebase

import com.workfort.pstuian.data.model.UserPresenceDto
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.ServerValue
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirebaseUserPresenceDataSource(database: FirebaseDatabase) {

    private val userPresenceRef = database.reference("presence")
    private val connectedRef = database.reference(".info/connected")

    suspend fun observeAndSyncUserPresence(userId: String) {
        val userRef = userPresenceRef.child(userId)
        try {
            connectedRef.valueEvents.collectLatest { snapshot ->
                runCatching {
                    val isConnected = snapshot.value<Boolean?>() ?: false
                    if (isConnected) {
                        userRef.onDisconnect().removeValue()
                        userRef.setValue(mapOf("lastSeenAt" to ServerValue.TIMESTAMP))
                    }
                }
            }
        } finally {
            withContext(NonCancellable) {
                runCatching { userRef.removeValue() }
            }
        }
    }

    fun observeUserPresence(userId: String): Flow<UserPresenceDto?> {
        return userPresenceRef.child(userId)
            .valueEvents
            .map { snapshot -> snapshot.value<UserPresenceDto?>() }
            .distinctUntilChanged()
    }

    suspend fun removeUserPresence(userId: String) {
        val userRef = userPresenceRef.child(userId)
        runCatching { userRef.removeValue() }
    }

    suspend fun getUserPresence(userId: String): UserPresenceDto? {
        val snapshot = userPresenceRef.child(userId)
            .valueEvents
            .filterNotNull()
            .first()

        return snapshot.value<UserPresenceDto?>()
    }

    fun observeActiveUsersPresence(): Flow<List<Pair<String, UserPresenceDto>>> {
        return userPresenceRef.valueEvents.map { snapshot ->
            snapshot.children
                .mapNotNull { child ->
                    val userId = child.key
                    val presence = child.value<UserPresenceDto?>() ?: return@mapNotNull null
                    userId?.let { it to presence }
                }
        }
    }
}
