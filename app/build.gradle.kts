plugins {
    id("chart.application")
}

dependencies {
    add("implementation", project(":presenter:home"))
    add("implementation", project(":presenter:main"))
    add("implementation", project(":data:repository"))
    add("implementation", project(":data:datasource"))
}