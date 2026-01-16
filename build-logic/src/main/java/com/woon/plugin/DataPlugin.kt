package com.woon.plugin

import com.android.build.gradle.LibraryExtension
import com.woon.base.androidProject
import com.woon.base.junitProject
import com.woon.base.kotlinProject
import com.woon.ext.applyPlugin
import com.woon.ext.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import java.util.Properties

class DataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                applyPlugin("android-library")
                applyPlugin("kotlin-android")
                apply("chart.hilt")
            }

            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())

            extensions.configure<LibraryExtension> {
                androidProject(this)
                kotlinProject(this)
                junitProject(this)
            }

            dependencies {
                "implementation"(findLibrary("retrofit"))
                "implementation"(findLibrary("retrofit-converter-gson"))
                "implementation"(findLibrary("okhttp3"))
                "implementation"(findLibrary("okhttp3-logging-interceptor"))
                add("implementation", project(":domain"))
                add("implementation", project(":network"))
            }
        }
    }
}