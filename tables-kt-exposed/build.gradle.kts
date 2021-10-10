plugins {
    id("ru.mipt.npm.gradle.jvm")
}

val exposedVersion = "0.35.1"

dependencies {
    api("org.jetbrains.exposed:exposed-core:$exposedVersion")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
    api(rootProject)
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.PROTOTYPE
}
