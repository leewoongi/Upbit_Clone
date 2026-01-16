package com.woon.base

import com.android.build.api.dsl.CommonExtension
import com.woon.ext.findLibrary
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension


/**
 * kotlin {         // ← KotlinExtension
 *     jvmTarget = "17"
 * }
 */
internal fun Project.kotlinProject(
    commonExtension: CommonExtension<*, *, *, *, *, *>
){
    commonExtension.apply {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        dependencies {
            "implementation"(findLibrary("androidx-core-ktx"))
            "implementation"(findLibrary("androidx-lifecycle-runtime-ktx"))
        }
    }
}
