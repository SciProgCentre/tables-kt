plugins {
    id("space.kscience.gradle.jvm")
    `maven-publish`
}

val exposedVersion = "0.36.1"

dependencies {
    api("org.jetbrains.exposed:exposed-core:$exposedVersion")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.slf4j:slf4j-simple:1.7.30")
    api(rootProject)
}

readme {
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}
