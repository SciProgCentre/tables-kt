import space.kscience.gradle.isInDevelopment
import space.kscience.gradle.useApache2Licence
import space.kscience.gradle.useSPCTeam

plugins {
    id("space.kscience.gradle.project")
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

description = "A lightweight multiplatform library for tables"

allprojects {
    group = "space.kscience"
    version = "0.2.0-dev-4"
}

val dataforgeVersion = "0.6.1"

kscience{
    jvm()
    js()
    native()
    useContextReceivers()
    dependencies {
        api("space.kscience:dataforge-io:$dataforgeVersion")
    }
}

ksciencePublish {
    pom("https://github.com/SciProgCentre/tables-kt") {
        useApache2Licence()
        useSPCTeam()
    }
    github("kmath", "tables-kt")
    space(
        if (isInDevelopment) {
            "https://maven.pkg.jetbrains.space/spc/p/sci/dev"
        } else {
            "https://maven.pkg.jetbrains.space/spc/p/sci/maven"
        }
    )
    sonatype("https://oss.sonatype.org")
}

readme {
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
    propertyByTemplate("artifact", rootProject.file("docs/ARTIFACT-TEMPLATE.md"))
}
