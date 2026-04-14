package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import kotlinx.coroutines.flow.Flow

interface SharedPrefRepository {
    fun getString(key: SharedPrefKey, defaultValue: String? = null): String?
    fun putString(key: SharedPrefKey, value: String?)
    fun getInt(key: SharedPrefKey, defaultValue: Int = 0): Int
    fun putInt(key: SharedPrefKey, value: Int)
    fun getLong(key: SharedPrefKey, defaultValue: Long = 0L): Long
    fun putLong(key: SharedPrefKey, value: Long)
    fun getDouble(key: SharedPrefKey, defaultValue: Double = 0.0): Double
    fun putDouble(key: SharedPrefKey, value: Double)
    fun observeDouble(key: SharedPrefKey, defaultValue: Double = 0.0): Flow<Double>
    fun getBoolean(key: SharedPrefKey, defaultValue: Boolean = false): Boolean
    fun putBoolean(key: SharedPrefKey, value: Boolean)
    fun remove(key: SharedPrefKey)
    fun clear(clearOnLogoutOnly: Boolean = false)
}
