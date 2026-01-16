package com.woon.ext

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal fun Project.applyPlugin(pluginName: String) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val pluginId = libs.findPlugin(pluginName).get().get().pluginId
    plugins.apply(pluginId)
}

internal fun Project.findLibrary(name: String): Provider<MinimalExternalModuleDependency> {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    return libs.findLibrary(name).get()
}