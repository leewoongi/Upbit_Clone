plugins {
    id("chart.core")
}

android {
    namespace = "com.woon.chart.ui"
}

dependencies {
    implementation(project(":chart-core"))
}
