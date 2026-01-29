package com.woon.chart.provider

import com.woon.chart.BuildConfig
import com.woon.domain.event.provider.AppInfoProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInfoProviderImpl @Inject constructor() : AppInfoProvider {

    override val appVersion: String
        get() = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"

    override val buildType: String
        get() = BuildConfig.BUILD_TYPE
}
