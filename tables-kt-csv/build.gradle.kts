plugins {
    id("space.kscience.gradle.mpp")
    `maven-publish`
}

kscience {
    jvm()
    js()
    commonMain {
        api(rootProject)
        api("com.github.doyaaaaaken:kotlin-csv:1.9.3")
    }
}

readme {
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}