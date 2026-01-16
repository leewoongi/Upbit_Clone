package com.woon.plugin

import com.woon.ext.applyPlugin
import com.woon.ext.findLibrary
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.gradle.kotlin.dsl.dependencies


class DomainPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply {
                applyPlugin("ksp")
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(17)
            }

            dependencies {
                "implementation"(findLibrary("hilt-core"))
                "implementation"(findLibrary("kotlinx-coroutines-core"))
                add("ksp", findLibrary("hilt-android-compiler"))
            }
        }
    }
}