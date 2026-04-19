package com.workfort.pstuian.data.remote.firebase

import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.ServerValue
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirebaseActiveUserDataSource(database: FirebaseDatabase) {

    private val activeUsersRef = database.reference("presence")
    private val connectedRef = database.reference(".info/connected")

    suspend fun registerUserPresence(userId: String) {
        val userRef = activeUsersRef.child(userId)
        try {
            connectedRef.valueEvents.collectLatest { snapshot ->
                runCatching {
                    val isConnected = snapshot.value<Boolean?>() ?: false
                    if (isConnected) {
                        userRef.onDisconnect().removeValue()

                        // Use ServerValue.TIMESTAMP to avoid device clock issues
                        userRef.setValue(ServerValue.TIMESTAMP)
                    }
                }
            }
        } finally {
            withContext(NonCancellable) {
                runCatching { userRef.removeValue() }
            }
        }
    }

    suspend fun removeUserPresence(userId: String) {
        val userRef = activeUsersRef.child(userId)
        runCatching { userRef.removeValue() }
    }

    fun observeActiveUsers(): Flow<List<Pair<String, Long>>> {
        // We order by value (the timestamp) and then reverse the list in memory
        // to show the most recently active users at the top.
        return activeUsersRef.orderByValue().valueEvents.map { snapshot ->
            snapshot.children
                .mapNotNull { child ->
                    val userId = child.key
                    val timestamp = child.value<Double?>()?.toLong() ?: 0L
                    userId?.let { it to timestamp }
                }
                .sortedByDescending { it.second }
        }
    }
}
