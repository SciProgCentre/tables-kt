plugins {
    id("space.kscience.gradle.jvm")
    `maven-publish`
}

val exposedVersion = "0.41.1"

dependencies {
    api("org.jetbrains.exposed:exposed-core:$exposedVersion")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    testImplementation("com.h2database:h2:2.2.224")
    testImplementation("org.slf4j:slf4j-simple:2.0.7")
    api(rootProject)
}

readme {
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}
