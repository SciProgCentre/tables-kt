plugins {
    id("ru.mipt.npm.gradle.jvm")
    `maven-publish`
}

val dataFrameVersion = "0.8.0-dev-781-0.11.0.39"

dependencies {
    api("org.jetbrains.kotlinx:dataframe:$dataFrameVersion")
    api(rootProject)
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.PROTOTYPE
}
