plugins {
    id("ru.mipt.npm.gradle.project")
    id("ru.mipt.npm.gradle.mpp")
    id("ru.mipt.npm.gradle.native")
    `maven-publish`
}

description = "A lightweight multiplatform library for tables"

allprojects {
    group = "space.kscience"
    version = "0.2.0-dev-1"

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        kotlinOptions{
            freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
        }
    }
}

val dataforgeVersion = "0.6.0-dev-10"

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
