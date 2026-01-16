package com.woon.plugin

import com.android.build.gradle.LibraryExtension
import com.woon.base.androidProject
import com.woon.base.composeProject
import com.woon.base.junitProject
import com.woon.base.kotlinProject
import com.woon.ext.applyPlugin
import com.woon.ext.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class FeaturePlugin : Plugin<Project>{
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                applyPlugin("android-library")
                applyPlugin("kotlin-android")
                applyPlugin("kotlin-compose")
                apply("chart.hilt")
            }

            extensions.configure<LibraryExtension> {
                androidProject(this)
                kotlinProject(this)
                junitProject(this)
                composeProject(this)
            }

            dependencies {
                add("implementation", project(":domain"))
                add("implementation", project(":core"))
            }
        }
    }
}