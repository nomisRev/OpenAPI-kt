package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.transform.ModelToIr
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationAnnotationsTest {

  @Test
  fun enum_capitalized_values_no_serial_name_full_match() {
    val enumModel =
      Model.Enum.Closed(
        NamingContext.Named("AutoOrManual"),
        Model.Primitive.String(null, null, null),
        listOf("Auto", "Manual"),
        default = "Auto",
        description = null,
      )
    val file =
      with(ModelToIr) {
        listOf<Model>(enumModel).toIrFile(fileName = "Models.kt", pkg = "com.example")
      }

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.Serializable

            @Serializable
            enum class AutoOrManual {
                Auto,
                Manual
            }
            """
        .trimIndent() + "\n")

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }

  @Test
  fun enum_lowercase_values_with_serial_name_full_match() {
    val lowerEnum =
      Model.Enum.Closed(
        NamingContext.Named("Lower"),
        Model.Primitive.String(null, null, null),
        listOf("auto", "manual"),
        default = "auto",
        description = null,
      )
    val file =
      with(ModelToIr) {
        listOf<Model>(lowerEnum).toIrFile(fileName = "Models.kt", pkg = "com.example")
      }

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.SerialName
            import kotlinx.serialization.Serializable

            @Serializable
            enum class Lower {
                @SerialName("auto")
                Auto,
                @SerialName("manual")
                Manual
            }
            """
        .trimIndent() + "\n")

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }

  @Test
  fun data_class_serializable_and_optionality_full_match() {
    val withDefault =
      Model.Object.Property(
        baseName = "age",
        model = Model.Primitive.Int(default = 42, description = null, constraint = null),
        isRequired = false,
        isNullable = false,
        description = null,
      )
    val withoutDefault =
      Model.Object.Property(
        baseName = "nickname",
        model = Model.Primitive.String(default = null, description = null, constraint = null),
        isRequired = false,
        isNullable = false,
        description = null,
      )
    val requiredNullable =
      Model.Object.Property(
        baseName = "alias",
        model = Model.Primitive.String(default = null, description = null, constraint = null),
        isRequired = true,
        isNullable = true,
        description = null,
      )

    val obj =
      Model.Object(
        context = NamingContext.Named("User"),
        description = null,
        properties = listOf(withDefault, withoutDefault, requiredNullable),
        inline = emptyList(),
      )
    val file =
      with(ModelToIr) { listOf<Model>(obj).toIrFile(fileName = "Models.kt", pkg = "com.example") }

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.Serializable

            @Serializable
            data class User(val age: Int = 42, val nickname: String? = null, val alias: String?)
            """
        .trimIndent() + "\n")

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }

  @Test
  fun free_form_json_maps_full_match() {
    val jsonProp =
      Model.Object.Property(
        baseName = "json",
        model = Model.FreeFormJson(description = null, constraint = null),
        isRequired = false,
        isNullable = true,
        description = null,
      )
    val obj =
      Model.Object(
        context = NamingContext.Named("Payload"),
        description = null,
        properties = listOf(jsonProp),
        inline = emptyList(),
      )
    val file =
      with(ModelToIr) { listOf<Model>(obj).toIrFile(fileName = "Models.kt", pkg = "com.example") }

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.Serializable
            import kotlinx.serialization.json.JsonElement

            @Serializable
            data class Payload(val json: JsonElement? = null)
            """
        .trimIndent() + "\n")

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }

  @Test
  fun empty_collection_defaults_full_match() {
    val listProp =
      Model.Object.Property(
        baseName = "names",
        model =
          Model.Collection.List(
            inner = Model.Primitive.String(null, null, null),
            default = emptyList(),
            description = null,
            constraint = null,
          ),
        isRequired = false,
        isNullable = true,
        description = null,
      )
    val setProp =
      Model.Object.Property(
        baseName = "tags",
        model =
          Model.Collection.Set(
            inner = Model.Primitive.String(null, null, null),
            default = emptyList(),
            description = null,
            constraint = null,
          ),
        isRequired = false,
        isNullable = true,
        description = null,
      )
    val obj =
      Model.Object(
        context = NamingContext.Named("Collections"),
        description = null,
        properties = listOf(listProp, setProp),
        inline = emptyList(),
      )
    val file =
      with(ModelToIr) { listOf<Model>(obj).toIrFile(fileName = "Models.kt", pkg = "com.example") }

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.Serializable

            @Serializable
            data class Collections(val names: List<String>? = emptyList(), val tags: Set<String>? = setOf())
            """
        .trimIndent() + "\n")

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }

  @Test
  fun required_with_schema_default_emits_required_annotation_full_match() {
    val prop =
      Model.Object.Property(
        baseName = "age",
        model = Model.Primitive.Int(default = 42, description = null, constraint = null),
        isRequired = true,
        isNullable = false,
        description = null,
      )
    val obj =
      Model.Object(
        context = NamingContext.Named("User"),
        description = null,
        properties = listOf(prop),
        inline = emptyList(),
      )
    val file =
      with(ModelToIr) { listOf<Model>(obj).toIrFile(fileName = "Models.kt", pkg = "com.example") }

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.Required
            import kotlinx.serialization.Serializable

            @Serializable
            data class User(@Required val age: Int = 42)
            """
        .trimIndent() + "\n")

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
