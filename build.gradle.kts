plugins {
    id("ru.mipt.npm.gradle.project")
    id("ru.mipt.npm.gradle.mpp")
    id("ru.mipt.npm.gradle.native")
}

allprojects {
    apply(plugin = "maven-publish")
    repositories{
        mavenLocal()
    }
    group = "space.kscience"
    version = "0.1.1-dev-1"
}

val dataforgeVersion = "0.5.2-dev-2"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("space.kscience:dataforge-context:$dataforgeVersion")
                api("space.kscience:dataforge-io:$dataforgeVersion")

                api(kotlin("reflect"))
            }
        }
    }
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.PROTOTYPE
}

ksciencePublish {
    github("dataforge-core")
    space("https://maven.pkg.jetbrains.space/mipt-npm/p/sci/maven")
    sonatype()
}
