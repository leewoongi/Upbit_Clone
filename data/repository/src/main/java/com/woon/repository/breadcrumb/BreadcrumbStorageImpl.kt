package com.woon.repository.breadcrumb

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woon.datasource.local.preferences.LocalPreferencesDataSource
import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.storage.BreadcrumbStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreadcrumbStorageImpl @Inject constructor(
    private val prefs: LocalPreferencesDataSource
) : BreadcrumbStorage {

    private val gson = Gson()

    override fun saveSnapshot(breadcrumbs: List<Breadcrumb>) {
        val json = gson.toJson(breadcrumbs)
        prefs.putString(KEY_SNAPSHOT, json)
        prefs.putLong(KEY_SNAPSHOT_TIME, System.currentTimeMillis())
    }

    override fun loadSnapshot(): Pair<List<Breadcrumb>, Long>? {
        val json = prefs.getString(KEY_SNAPSHOT) ?: return null
        val time = prefs.getLong(KEY_SNAPSHOT_TIME)

        val breadcrumbs = runCatching {
            val type = object : TypeToken<List<Breadcrumb>>() {}.type
            gson.fromJson<List<Breadcrumb>>(json, type)
        }.getOrNull() ?: return null

        return breadcrumbs to time
    }

    override fun clearSnapshot() {
        prefs.remove(KEY_SNAPSHOT)
        prefs.remove(KEY_SNAPSHOT_TIME)
    }

    companion object {
        private const val KEY_SNAPSHOT = "breadcrumb_snapshot"
        private const val KEY_SNAPSHOT_TIME = "breadcrumb_snapshot_time"
    }
}
