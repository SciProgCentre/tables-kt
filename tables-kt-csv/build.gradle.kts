plugins {
    id("ru.mipt.npm.gradle.jvm")
    `maven-publish`
}

dependencies {
    api(rootProject)
    implementation("org.apache.commons:commons-csv:1.9.0")
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.EXPERIMENTAL
}