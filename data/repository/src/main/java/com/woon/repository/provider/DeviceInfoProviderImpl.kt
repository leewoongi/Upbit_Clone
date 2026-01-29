package com.woon.repository.provider

import android.os.Build
import com.woon.domain.event.provider.DeviceInfoProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceInfoProviderImpl @Inject constructor() : DeviceInfoProvider {

    override val deviceModel: String
        get() = Build.MODEL

    override val osSdkInt: Int
        get() = Build.VERSION.SDK_INT
}
