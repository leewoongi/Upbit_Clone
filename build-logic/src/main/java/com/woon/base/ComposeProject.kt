package com.woon.base

import com.android.build.api.dsl.CommonExtension
import com.woon.ext.findLibrary
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.composeProject(
    commonExtension: CommonExtension<*, *, *, *, *, *>
){
    commonExtension.apply {
        buildFeatures {
            compose = true
            buildConfig = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.15"
        }

        dependencies {
            val bom = findLibrary("androidx-compose-bom")
            "implementation"(platform(bom))
            "implementation"(findLibrary("androidx-ui-tooling-preview"))
            "implementation"(findLibrary("androidx-activity-compose"))
            "implementation"(findLibrary("androidx-ui"))
            "implementation"(findLibrary("androidx-ui-graphics"))
            "implementation"(findLibrary("androidx-ui-tooling-preview"))
            "implementation"(findLibrary("androidx-material3"))
            "implementation"(findLibrary("androidx-navigation"))

            "androidTestImplementation"(platform(bom))
            "androidTestImplementation"(findLibrary("androidx-ui-test-junit4"))

            "debugImplementation"(findLibrary("androidx-ui-tooling"))
            "debugImplementation"(findLibrary("androidx-ui-test-manifest"))
        }
    }
}