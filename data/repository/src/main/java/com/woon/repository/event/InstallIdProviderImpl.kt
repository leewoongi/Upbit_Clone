package com.woon.repository.event

import com.woon.datasource.local.preferences.LocalPreferencesDataSource
import com.woon.domain.event.provider.InstallIdProvider
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstallIdProviderImpl @Inject constructor(
    private val prefs: LocalPreferencesDataSource
) : InstallIdProvider {

    override val installId: String
        get() {
            val existing = prefs.getString(KEY_INSTALL_ID)
            if (existing != null) return existing

            val newId = UUID.randomUUID().toString()
            prefs.putString(KEY_INSTALL_ID, newId)
            return newId
        }

    companion object {
        private const val KEY_INSTALL_ID = "install_id"
    }
}
