package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.OpenAPI
import kotlinx.serialization.Serializable
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

private fun BufferedSink.writeUtf8Line(line: String) {
  writeUtf8("$line\n")
}

private fun BufferedSink.writeUtf8Line() {
  writeUtf8("\n")
}

public fun FileSystem.test(
  pathSpec: String,
  `package`: String = "io.github.nomisrev.openapi",
  modelPackage: String = "$`package`.models",
  generationPath: String =
    "build/generated/openapi/src/commonMain/kotlin/${`package`.replace(".", "/")}"
) {
  fun file(name: String, imports: Set<String>, code: String) {
    write("$generationPath/models/$name.kt".toPath()) {
      writeUtf8Line("package $modelPackage")
      writeUtf8Line()
//      if (imports.isNotEmpty()) {
//        writeUtf8Line(imports.joinToString("\n") { "import ${it.`package`}.${it.typeName}" })
//        writeUtf8Line()
//      }
      writeUtf8Line(code)
    }
  }

  deleteRecursively(generationPath.toPath())
  createDirectories("$generationPath/models".toPath())
  val rawSpec = source(pathSpec.toPath()).buffer().use(BufferedSource::readUtf8)
  val openAPI = OpenAPI.fromJson(rawSpec)
  openAPI.models().forEach { model ->
    file(model.typeName(), setOf(), template { toCode(model) })
  }
}

public data class TopLevel(val key: String, val model: KModel)

/**
 * Our own "Generated" oriented KModel.
 * The goal of this KModel is to make generation as easy as possible,
 * so we gather all information ahead of time.
 *
 * This KModel can/should be updated overtime to include all information we need for code generation.
 */
public sealed interface KModel {
  public enum class Primitive : KModel {
    Int, Double, Boolean, String, Unit;
  }

  public data object Binary : KModel
  public data object JsonObject : KModel

  public sealed interface Collection : KModel {
    public val value: KModel

    public data class List(override val value: KModel) : Collection {
      val simpleName: String = "List"
    }

    public data class Set(override val value: KModel) : Collection {
      val simpleName: String = "Set"
    }

    public data class Map(override val value: KModel) : Collection {
      public val key: Primitive = Primitive.String
      val simpleName: String = "Map"
    }
  }

  @Serializable
  public data class Object(
    val simpleName: String,
    val description: String?,
    val properties: List<Property>,
    val inline: List<KModel>
  ) : KModel {
    @Serializable
    public data class Property(
      val baseName: String,
      val name: String,
      val type: KModel,
      /**
       * isRequired != not-null.
       * This means the value _has to be included_ in the payload,
       * but it might be [isNullable].
       */
      val isRequired: Boolean,
      val isNullable: Boolean,
      val description: String?,
      val defaultValue: String?
    )
  }

  // TODO Currently doesn't deal with nested code
  //   When we have nested inline schemas, they should be generated in a nested way.
  public sealed interface Union : KModel {
    // TODO, get rid of simpleName? It's a Kotlin detail.
    public val simpleName: String
    public val schemas: List<UnionCase>

    public data class UnionCase(val caseName: String, val model: KModel)

    public data class OneOf(
      override val simpleName: String,
      override val schemas: List<UnionCase>
    ) : Union

    public data class AnyOf(
      override val simpleName: String,
      override val schemas: List<UnionCase>
    ) : Union

    public data class TypeArray(
      override val simpleName: String,
      override val schemas: List<UnionCase>,
    ) : Union
  }

  public data class Enum(
    val simpleName: String,
    val inner: KModel,
    val values: List<String>,
  ) : KModel
}
