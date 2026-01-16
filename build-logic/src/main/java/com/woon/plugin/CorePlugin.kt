package com.woon.plugin

import com.android.build.gradle.LibraryExtension
import com.woon.base.androidProject
import com.woon.base.composeProject
import com.woon.base.junitProject
import com.woon.base.kotlinProject
import com.woon.ext.applyPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Core 모듈 플러그인
 *
 * 프로젝트 전체에서 공통으로 사용되는 요소들을 포함하는 모듈을 위한 플러그인입니다.
 * - UI 테마 (Theme, Color, Typography)
 * - 공통 유틸리티
 * - 확장 함수
 * - 공통 Composable
 *
 * Feature 플러그인과 달리 domain 의존성을 포함하지 않습니다.
 */
class CorePlugin : Plugin<Project> {
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

            // Core 모듈은 domain에 의존하지 않음
            // 다른 모듈에서 core를 참조하도록 함
        }
    }
}