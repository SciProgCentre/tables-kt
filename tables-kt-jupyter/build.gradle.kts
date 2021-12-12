plugins {
    id("ru.mipt.npm.gradle.jvm")
    `maven-publish`
}

dependencies {
    api(rootProject)
    api(npmlibs.kotlinx.html)
}

kscience{
    jupyterLibrary("space.kscience.tables.TablesForJupyter")
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.EXPERIMENTAL
}
