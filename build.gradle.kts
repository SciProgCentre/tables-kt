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
    version = "0.4.0"
}

val dataforgeVersion = "0.8.0"

kscience{
    jvm()
    js()
    native()
    wasm()
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
