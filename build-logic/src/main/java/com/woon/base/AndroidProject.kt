package com.woon.base

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
// 빌드 로직은 뭔지, 왜 사용하는지
/**
 * android {        // ← AndroidExtension
 *     compileSdk = 34
 * }
 */
internal fun Project.androidProject(
    commonExtension: CommonExtension<*, *, *, *, *, *>
){
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 24
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
}