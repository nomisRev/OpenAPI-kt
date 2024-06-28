/**
 * This file was taken from SqlDelight:
 * https://github.com/cashapp/sqldelight/blob/63ffbab7b24583be686a12406798d1e1bb8c420a/sqldelight-gradle-plugin/src/main/kotlin/app/cash/sqldelight/gradle/kotlin/SourceRoots.kt
 */
package io.github.nomisrev.openapi.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.crash.afterEvaluate
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.io.File

/**
 * @return A list of source roots and their dependencies.
 *
 * Examples:
 *   Multiplatform Environment. Ios target labeled "ios".
 *     -> iosMain deps [commonMain]
 *
 *   Android environment. internal, production, release, debug variants.
 *     -> internalDebug deps [internal, debug, main]
 *     -> internalRelease deps [internal, release, main]
 *     -> productionDebug deps [production, debug, main]
 *     -> productionRelease deps [production, release, main]
 *
 *    Multiplatform environment with android target (oh boy)
 */
internal fun sources(project: Project): List<Source> {
  project.extensions.findByType(KotlinMultiplatformExtension::class.java)?.let {
    return it.sources()
  }

  project.extensions.findByType(KotlinJsProjectExtension::class.java)?.let {
    return it.sources()
  }

  project.extensions.findByName("android")?.let {
    return (it as BaseExtension).sources(project)
  }

  val sourceSets = (project.extensions.getByName("kotlin") as KotlinProjectExtension).sourceSets
  return listOf(
    Source(
      type = KotlinPlatformType.jvm,
      name = "main",
      sourceSets = listOf("main"),
      sourceDirectorySet = sourceSets.getByName("main").kotlin,
      registerGeneratedDirectory = { outputDirectoryProvider ->
        sourceSets.getByName("main").kotlin.srcDirs(outputDirectoryProvider.get())
      }
    ),
  )
}

private fun KotlinJsProjectExtension.sources(): List<Source> {
  return listOf(
    Source(
      type = KotlinPlatformType.js,
      name = "main",
      sourceDirectorySet = sourceSets.getByName("main").kotlin,
      sourceSets = listOf("main"),
      registerGeneratedDirectory = { outputDirectoryProvider ->
        sourceSets.getByName("main").kotlin.srcDirs(outputDirectoryProvider.get())
      }
    ),
  )
}

private fun KotlinMultiplatformExtension.sources(): List<Source> {
  // For multiplatform we only support SQLDelight in commonMain - to support other source sets
  // we would need to generate expect/actual SQLDelight code which at least right now doesn't
  // seem like there is a use case for. However this code is capable of running on any Target type.
  return listOf(
    Source(
      type = KotlinPlatformType.common,
      nativePresetName = "common",
      name = "commonMain",
      variantName = null,
      sourceDirectorySet = sourceSets.getByName("commonMain").kotlin,
      sourceSets = listOf("commonMain"),
      registerGeneratedDirectory = { outputDirectoryProvider ->
        sourceSets.getByName("commonMain").kotlin.srcDirs(outputDirectoryProvider.get())
      },
    ),
  )
}

private fun BaseExtension.sources(project: Project): List<Source> {
  val variants = when (this) {
    is AppExtension -> applicationVariants
    is LibraryExtension -> libraryVariants
    else -> throw IllegalStateException("Unknown Android plugin $this")
  }
  val kotlinSourceSets = (project.extensions.getByName("kotlin") as KotlinProjectExtension).sourceSets
  val sourceSets = sourceSets
    .associate { sourceSet ->
      sourceSet.name to kotlinSourceSets.getByName(sourceSet.name).kotlin
    }

  return variants.map { variant ->
    Source(
      type = KotlinPlatformType.androidJvm,
      name = variant.name,
      variantName = variant.name,
      sourceDirectorySet = sourceSets[variant.name]!!,
      sourceSets = variant.sourceSets.map { it.name },
      registerGeneratedDirectory = { outputDirectoryProvider ->
        variant.addJavaSourceFoldersToModel(outputDirectoryProvider.get())
      },
    )
  }
}

internal data class Source(
  val type: KotlinPlatformType,
  val nativePresetName: String? = null,
  val sourceDirectorySet: SourceDirectorySet,
  val name: String,
  val variantName: String? = null,
  val sourceSets: List<String>,
  val registerGeneratedDirectory: ((Provider<File>) -> Unit),
)
