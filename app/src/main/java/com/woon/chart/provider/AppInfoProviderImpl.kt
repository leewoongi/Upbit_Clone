package com.woon.chart.provider

import com.woon.chart.BuildConfig
import com.woon.domain.event.provider.AppInfoProvider
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInfoProviderImpl @Inject constructor() : AppInfoProvider {

    override val appVersion: String
        get() = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"

    override val buildType: String
        get() = BuildConfig.BUILD_TYPE

    override val locale: String
        get() = Locale.getDefault().toString()

    override val buildFingerprint: String
        get() = "${BuildConfig.VERSION_NAME}-${BuildConfig.BUILD_TYPE}-${BuildConfig.VERSION_CODE}"
}
