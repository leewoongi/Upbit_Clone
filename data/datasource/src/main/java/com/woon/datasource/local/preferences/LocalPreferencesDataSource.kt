package com.woon.datasource.local.preferences

interface LocalPreferencesDataSource {
    fun putString(key: String, value: String)
    fun getString(key: String, defaultValue: String? = null): String?

    fun putLong(key: String, value: Long)
    fun getLong(key: String, defaultValue: Long = 0L): Long

    fun putInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    fun remove(key: String)
    fun clear()
}
