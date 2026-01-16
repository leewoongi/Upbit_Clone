plugins {
    id("chart.data")
}

android{
    namespace = "com.woongi.data.repository"
}

dependencies {
    implementation(project(":data:datasource"))
}