plugins {
    id("space.kscience.gradle.jvm")
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
    maturity = space.kscience.gradle.Maturity.EXPERIMENTAL
}
