package com.woon.plugin

import com.woon.ext.applyPlugin
import com.woon.ext.findLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply {
                applyPlugin("ksp")
                applyPlugin("hilt")
            }

            dependencies {
                "implementation"(findLibrary("hilt-android"))
                "implementation"(findLibrary("hilt-navigation-compose"))
                add("ksp", findLibrary("hilt-android-compiler"))
            }
        }
    }
}