package com.workfort.pstuian.data.local.keyvaluestorage

import platform.Foundation.NSUserDefaults

class IosKeyValueStorage : KeyValueStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun getString(key: String, defaultValue: String?): String? {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    override fun putString(key: String, value: String?) {
        userDefaults.setObject(value, forKey = key)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.integerForKey(key).toInt()
        } else {
            defaultValue
        }
    }

    override fun putInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), forKey = key)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.integerForKey(key)
        } else {
            defaultValue
        }
    }

    override fun putLong(key: String, value: Long) {
        userDefaults.setInteger(value, forKey = key)
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.doubleForKey(key)
        } else {
            defaultValue
        }
    }

    override fun putDouble(key: String, value: Double) {
        userDefaults.setDouble(value, forKey = key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (userDefaults.objectForKey(key) != null) {
            userDefaults.boolForKey(key)
        } else {
            defaultValue
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, forKey = key)
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun clear() {
        val dictionary = userDefaults.dictionaryRepresentation()
        dictionary.keys.forEach { key ->
            val keyString = key as? String
            if (keyString != null) {
                userDefaults.removeObjectForKey(keyString)
            }
        }
    }
}
