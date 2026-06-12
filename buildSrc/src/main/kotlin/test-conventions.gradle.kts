import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

plugins {
    id("org.jetbrains.kotlin.plugin.power-assert")
    id("org.jetbrains.kotlinx.kover")
}

@Suppress("OPT_IN_USAGE")
configure<PowerAssertGradleExtension> {
    functions = listOf(
        "kotlin.test.assertEquals",
        "kotlin.test.assertTrue",
        "kotlin.test.assertFalse",
        "io.github.nomisrev.Eq.Companion.invoke"
    )
}

