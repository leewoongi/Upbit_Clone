package com.woon.plugin

import com.woon.ext.applyPlugin
import com.woon.ext.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DatastorePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                applyPlugin("ksp")
            }

            dependencies {
                "implementation"(findLibrary("room-runtime"))
                "implementation"(findLibrary("room-ktx"))
                add("ksp", findLibrary("room-compiler"))
            }
        }
    }
}
