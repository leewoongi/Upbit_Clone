plugins {
    id("chart.feature")
}

android{
    namespace = "com.woon.presenter.main"
}

dependencies {
    implementation(project(":presenter:home"))
    implementation(project(":presenter:detail"))
}