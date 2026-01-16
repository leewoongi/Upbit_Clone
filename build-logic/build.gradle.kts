import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}


// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("Application") {
            id = "chart.application"
            implementationClass = "com.woon.plugin.ApplicationPlugin"
        }

        register("Feature") {
            id = "chart.feature"
            implementationClass = "com.woon.plugin.FeaturePlugin"
        }

        register("Hilt") {
            id = "chart.hilt"
            implementationClass = "com.woon.plugin.HiltPlugin"
        }

        register("Domain") {
            id = "chart.domain"
            implementationClass = "com.woon.plugin.DomainPlugin"
        }

        register("Data") {
            id = "chart.data"
            implementationClass = "com.woon.plugin.DataPlugin"
        }

        register("Core") {
            id = "chart.core"
            implementationClass = "com.woon.plugin.CorePlugin"
        }

        register("Network") {
            id = "chart.network"
            implementationClass = "com.woon.plugin.NetworkPlugin"
        }
    }
}
