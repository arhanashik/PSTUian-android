package com.workfort.pstuian.data.remote.firestore

import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.SystemNotificationDto
import com.workfort.pstuian.data.remote.firestore.CommonFields.FIELD_IS_PUBLISHED
import com.workfort.pstuian.data.remote.firestore.CommonFields.FIELD_READ_AT
import com.workfort.pstuian.data.remote.firestore.FirestorePaths.SYSTEM_NOTIFICATIONS_PATH
import com.workfort.pstuian.data.remote.firestore.FirestorePaths.USERS_PATH
import com.workfort.pstuian.data.remote.firestore.FirestorePaths.USER_SYSTEM_NOTIFICATIONS_PATH
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FirestoreSystemNotificationDataSource(private val firestore: FirebaseFirestore) {

    private val systemNotificationCollection = firestore.collection(SYSTEM_NOTIFICATIONS_PATH)

    fun observeSystemNotifications(): Flow<NetworkResult<List<Pair<String, SystemNotificationDto>>>> {
        return systemNotificationCollection
            .where { FIELD_IS_PUBLISHED equalTo true }
            .snapshots
            .map { snapshot ->
                val list = snapshot.documents.map { it.id to it.data<SystemNotificationDto>() }
                NetworkResult.success(list)
            }
            .catch { exception ->
                emit(NetworkResult.failure(CommonNetworkError.fireStoreInternalError(exception)))
            }
    }

    fun observeReadSystemNotificationIds(userId: String): Flow<List<String>> {
        return getUserSystemNotificationRef(userId)
            .snapshots
            .map { snapshot -> snapshot.documents.map { it.id } }
            .catch { emit(emptyList()) }
    }

    suspend fun markSystemNotificationAsRead(userId: String, notificationId: String): NetworkResult<Unit> {
        return try {
            getUserSystemNotificationRef(userId)
                .document(notificationId)
                .set(mapOf(FIELD_READ_AT to Timestamp.now()))

            NetworkResult.success(Unit)
        } catch (e: Exception) {
            NetworkResult.failure(CommonNetworkError.fireStoreInternalError(e))
        }
    }

    private fun getUserSystemNotificationRef(userId: String): CollectionReference {
        return firestore
            .collection(USERS_PATH)
            .document(userId)
            .collection(USER_SYSTEM_NOTIFICATIONS_PATH)
    }
}
