package com.workfort.pstuian.data.local.keyvaluestorage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AndroidKeyValueStorage(context: Context) : KeyValueStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("pstuian_prefs", Context.MODE_PRIVATE)

    override fun getString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    override fun putString(key: String, value: String?) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return Double.fromBits(sharedPreferences.getLong(key, defaultValue.toBits()))
    }

    override fun putDouble(key: String, value: Double) {
        sharedPreferences.edit { putLong(key, value.toBits()) }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    override fun remove(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }
}
