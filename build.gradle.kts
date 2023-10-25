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
    version = "0.2.1"
}

val dataforgeVersion = "0.6.2"

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
    repository("spc","https://maven.sciprog.center/kscience")
    sonatype("https://oss.sonatype.org")
}

readme {
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
    propertyByTemplate("artifact", rootProject.file("docs/ARTIFACT-TEMPLATE.md"))
}
