package com.woon.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.woon.base.applicationProject
import com.woon.base.junitProject
import com.woon.base.kotlinProject
import com.woon.ext.applyPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class ApplicationPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                applyPlugin("android-application")
                applyPlugin("kotlin-android")
                apply("chart.hilt")
            }

            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())

            extensions.configure<ApplicationExtension> {
                applicationProject(this)
                kotlinProject(this)
                junitProject(this)
            }
        }
    }
}