package com.woon.repository.breadcrumb

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.storage.BreadcrumbStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreadcrumbStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BreadcrumbStorage {

    private val gson = Gson()

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun saveSnapshot(breadcrumbs: List<Breadcrumb>) {
        val json = gson.toJson(breadcrumbs)
        prefs.edit()
            .putString(KEY_SNAPSHOT, json)
            .putLong(KEY_SNAPSHOT_TIME, System.currentTimeMillis())
            .apply()
    }

    override fun loadSnapshot(): Pair<List<Breadcrumb>, Long>? {
        val json = prefs.getString(KEY_SNAPSHOT, null) ?: return null
        val time = prefs.getLong(KEY_SNAPSHOT_TIME, 0)

        val breadcrumbs = runCatching {
            val type = object : TypeToken<List<Breadcrumb>>() {}.type
            gson.fromJson<List<Breadcrumb>>(json, type)
        }.getOrNull() ?: return null

        return breadcrumbs to time
    }

    override fun clearSnapshot() {
        prefs.edit()
            .remove(KEY_SNAPSHOT)
            .remove(KEY_SNAPSHOT_TIME)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "breadcrumb_prefs"
        private const val KEY_SNAPSHOT = "breadcrumb_snapshot"
        private const val KEY_SNAPSHOT_TIME = "breadcrumb_snapshot_time"
    }
}
