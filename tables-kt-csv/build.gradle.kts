plugins {
    id("space.kscience.gradle.jvm")
    `maven-publish`
}

dependencies {
    api(rootProject)
    implementation("org.apache.commons:commons-csv:1.10.0")
}

readme {
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}