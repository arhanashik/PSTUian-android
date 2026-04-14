package com.workfort.pstuian.data.local.keyvaluestorage

interface KeyValueStorage {
    fun getString(key: String, defaultValue: String?): String?
    fun putString(key: String, value: String?)
    fun getInt(key: String, defaultValue: Int): Int
    fun putInt(key: String, value: Int)
    fun getLong(key: String, defaultValue: Long): Long
    fun putLong(key: String, value: Long)
    fun getDouble(key: String, defaultValue: Double): Double
    fun putDouble(key: String, value: Double)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun remove(key: String)
    fun clear()
}
