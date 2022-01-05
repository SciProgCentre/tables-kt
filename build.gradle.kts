plugins {
    id("ru.mipt.npm.gradle.project")
    id("ru.mipt.npm.gradle.mpp")
    id("ru.mipt.npm.gradle.native")
    `maven-publish`
}

allprojects {
    group = "space.kscience"
    version = "0.1.3"
}

val dataforgeVersion = "0.5.2"

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api("space.kscience:dataforge-context:$dataforgeVersion")
                api("space.kscience:dataforge-io:$dataforgeVersion")

                //api(kotlin("reflect"))
            }
        }
    }
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.EXPERIMENTAL
}

ksciencePublish {
    github("dataforge-core")
    space("https://maven.pkg.jetbrains.space/mipt-npm/p/sci/maven")
    //sonatype()
}
