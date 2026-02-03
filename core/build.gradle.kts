plugins {
    id("chart.core")
}

android {
    namespace = "com.woon.core"
}

dependencies {
    implementation(libs.okhttp3)
    implementation(libs.gson)

    // Test dependencies for retrofit HttpException testing
    testImplementation(libs.retrofit)
}