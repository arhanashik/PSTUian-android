package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.local.keyvaluestorage.KeyValueStorage
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class SharedPrefRepositoryImpl(private val storage: KeyValueStorage) : SharedPrefRepository {

    private val preferenceFlow = MutableSharedFlow<Pair<SharedPrefKey, Any?>>(extraBufferCapacity = 1)

    override fun getString(key: SharedPrefKey, defaultValue: String?): String? {
        return storage.getString(key.name, defaultValue)
    }

    override fun putString(key: SharedPrefKey, value: String?) {
        storage.putString(key.name, value)
    }

    override fun getInt(key: SharedPrefKey, defaultValue: Int): Int {
        return storage.getInt(key.name, defaultValue)
    }

    override fun putInt(key: SharedPrefKey, value: Int) {
        storage.putInt(key.name, value)
    }

    override fun getLong(key: SharedPrefKey, defaultValue: Long): Long {
        return storage.getLong(key.name, defaultValue)
    }

    override fun putLong(key: SharedPrefKey, value: Long) {
        storage.putLong(key.name, value)
    }

    override fun getDouble(key: SharedPrefKey, defaultValue: Double): Double {
        return storage.getDouble(key.name, defaultValue)
    }

    override fun putDouble(key: SharedPrefKey, value: Double) {
        storage.putDouble(key.name, value)
        preferenceFlow.tryEmit(key to value)
    }

    override fun observeDouble(key: SharedPrefKey, defaultValue: Double): Flow<Double> {
        return preferenceFlow
            .onStart { emit(key to getDouble(key, defaultValue)) }
            .filter { it.first == key }
            .map { it.second as Double }
    }

    override fun getBoolean(key: SharedPrefKey, defaultValue: Boolean): Boolean {
        return storage.getBoolean(key.name, defaultValue)
    }

    override fun putBoolean(key: SharedPrefKey, value: Boolean) {
        storage.putBoolean(key.name, value)
    }

    override fun remove(key: SharedPrefKey) {
        storage.remove(key.name)
    }

    override fun clear(clearOnLogoutOnly: Boolean) {
        if (clearOnLogoutOnly) {
            SharedPrefKey.getAllKeys()
                .filter { it.clearOnSignOut }
                .forEach { storage.remove(it.name) }
        } else {
            storage.clear()
        }
    }
}
