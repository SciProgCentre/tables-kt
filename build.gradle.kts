plugins {
    id("ru.mipt.npm.gradle.project")
    id("ru.mipt.npm.gradle.mpp")
    id("ru.mipt.npm.gradle.native")
    `maven-publish`
}

description = "A lightweight multiplatform library for tables"

allprojects {
    group = "space.kscience"
    version = "0.1.5"
}

val dataforgeVersion = "0.5.2"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("space.kscience:dataforge-io:$dataforgeVersion")
            }
        }
    }
}

ksciencePublish {
    github("tables-kt")
    space("https://maven.pkg.jetbrains.space/mipt-npm/p/sci/maven")
    //sonatype()
}


readme {
    maturity = ru.mipt.npm.gradle.Maturity.EXPERIMENTAL
    propertyByTemplate("artifact", rootProject.file("docs/ARTIFACT-TEMPLATE.md"))
}
