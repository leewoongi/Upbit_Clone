plugins {
    id("chart.feature")
}

android{
    namespace = "com.woongi.presenter.main"
}

dependencies {
    implementation(project(":presenter:home"))
}