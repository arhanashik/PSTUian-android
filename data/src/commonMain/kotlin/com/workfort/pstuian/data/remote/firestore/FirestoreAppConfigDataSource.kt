package com.workfort.pstuian.data.remote.firestore

import com.workfort.pstuian.data.model.AppConfigDto
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.firestore.FirestorePaths.APP_CONFIG_PATH
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirestoreAppConfigDataSource(private val firestore: FirebaseFirestore) {

    fun observe(): Flow<Pair<String, AppConfigDto>?> {
        return firestore.collection(APP_CONFIG_PATH)
            .snapshots
            .map { snapshot ->
                snapshot.documents
                    .map { it.id to it.data<AppConfigDto>() }
                    .firstOrNull()
            }
    }

    suspend fun get() : Pair<String, AppConfigDto>? {
        return try {
            firestore.collection(APP_CONFIG_PATH)
                .get()
                .documents
                .map { it.id to it.data<AppConfigDto>() }
                .firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun update(configId: String, config: AppConfigDto): NetworkResult<Unit> {
        return try {
            firestore.collection(APP_CONFIG_PATH)
                .document(configId)
                .update(config)
            NetworkResult.success(Unit)
        } catch (exception: Exception) {
            NetworkResult.failure(CommonNetworkError.fireStoreInternalError(exception))
        }
    }

    suspend fun clearFirestoreCache(): NetworkResult<Unit> {
        return try {
            firestore.clearPersistence()
            NetworkResult.Success(Unit)
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }
}
