plugins {
    id("space.kscience.gradle.jvm")
    `maven-publish`
}

val dataFrameVersion = "0.10.0"

dependencies {
    api("org.jetbrains.kotlinx:dataframe:$dataFrameVersion")
    api(rootProject)
}

readme {
    maturity = space.kscience.gradle.Maturity.PROTOTYPE
}
