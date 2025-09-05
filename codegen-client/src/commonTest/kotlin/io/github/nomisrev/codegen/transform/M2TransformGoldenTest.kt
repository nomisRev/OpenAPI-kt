package io.github.nomisrev.codegen.transform

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class M2TransformGoldenTest {
  @Test
  fun data_class_required_optional_defaults() {
    val ctx = NamingContext.Named("Person")
    val model =
      Model.Object(
        context = ctx,
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "name",
              model =
                Model.Primitive.String(
                  default = "John Doe",
                  description = "The name",
                  constraint = null,
                ),
              isRequired = true,
              isNullable = false,
              description = "The name",
            ),
            Model.Object.Property(
              baseName = "age",
              model = Model.Primitive.Int(default = null, description = null, constraint = null),
              isRequired = false,
              isNullable = false,
              description = null,
            ),
            Model.Object.Property(
              baseName = "active",
              model = Model.Primitive.Boolean(default = null, description = null),
              isRequired = true,
              isNullable = true,
              description = null,
            ),
            // keyword property to verify escaping
            Model.Object.Property(
              baseName = "object",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
          ),
        inline = emptyList(),
      )

    val file = listOf<Model>(model).toIrFile(pkg = "com.example")
    val actual = emitFile(file)

    val expected =
      ("""
      package com.example

      import kotlinx.serialization.Serializable
      import kotlinx.serialization.Required

      @Serializable
      data class Person(@Required val name: String = "John Doe", val age: Int? = null, val active: Boolean?, val `object`: String)
      """
        .trimIndent() + "\n")

    assertEquals(expected, actual)
  }

  @Test
  fun data_class_collections_and_defaults() {
    val ctx = NamingContext.Named("CollectionsHolder")
    val model =
      Model.Object(
        context = ctx,
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "tags",
              model =
                Model.Collection.List(
                  inner =
                    Model.Primitive.String(default = null, description = null, constraint = null),
                  default = listOf("a", "b"),
                  description = null,
                  constraint = null,
                ),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
            Model.Object.Property(
              baseName = "labels",
              model =
                Model.Collection.List(
                  inner =
                    Model.Primitive.String(default = null, description = null, constraint = null),
                  default = emptyList(),
                  description = null,
                  constraint = null,
                ),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
            Model.Object.Property(
              baseName = "attributes",
              model =
                Model.Collection.Map(
                  inner =
                    Model.Primitive.String(default = null, description = null, constraint = null),
                  description = null,
                  constraint = null,
                ),
              isRequired = false,
              isNullable = false,
              description = null,
            ),
          ),
        inline = emptyList(),
      )

    val file = listOf<Model>(model).toIrFile(pkg = "com.example")
    val actual = emitFile(file)

    val expected =
      ("""
      package com.example

      import kotlinx.serialization.Serializable
      import kotlinx.serialization.Required

      @Serializable
      data class CollectionsHolder(@Required val tags: List<String> = listOf("a", "b"), @Required val labels: List<String> = emptyList(), val attributes: Map<String, String>? = null)
      """
        .trimIndent() + "\n")

    assertEquals(expected, actual)
  }

  @Test
  fun enum_and_default_on_property() {
    val enumCtx = NamingContext.Named("OrderStatus")
    val enumModel =
      Model.Enum.Closed(
        context = enumCtx,
        inner = Model.Primitive.String(default = null, description = null, constraint = null),
        values = listOf("pending", "in-progress", "done"),
        default = "in-progress",
        description = null,
      )
    val obj =
      Model.Object(
        context = NamingContext.Named("Ticket"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "status",
              model = Model.Reference(enumCtx, description = null),
              isRequired = true,
              isNullable = false,
              description = null,
            )
          ),
        inline = emptyList(),
      )

    val file = listOf<Model>(enumModel, obj).toIrFile(pkg = "com.example")
    val actual = emitFile(file)

    val expected =
      ("""
      package com.example

      import kotlinx.serialization.Serializable
      import kotlinx.serialization.SerialName
      import kotlinx.serialization.Required

      @Serializable
      enum class OrderStatus {
          @SerialName("pending")
          Pending,
          @SerialName("in-progress")
          InProgress,
          @SerialName("done")
          Done
      }

      @Serializable
      data class Ticket(@Required val status: OrderStatus = OrderStatus.InProgress)
      """
        .trimIndent() + "\n")

    assertEquals(expected, actual)
  }

  @Test
  fun numeric_enum_int_values_serialname_only() {
    val enumCtx = NamingContext.Named("ErrorCode")
    val enumModel =
      Model.Enum.Closed(
        context = enumCtx,
        inner = Model.Primitive.Int(default = null, description = null, constraint = null),
        values = listOf("1", "2", "404"),
        default = "2",
        description = null,
      )

    val file = listOf<Model>(enumModel).toIrFile(pkg = "com.example")
    val actual = emitFile(file)

    val expected =
      ("""
      package com.example

      import kotlinx.serialization.Serializable
      import kotlinx.serialization.SerialName

      @Serializable
      enum class ErrorCode {
          @SerialName("1")
          _1,
          @SerialName("2")
          _2,
          @SerialName("404")
          _404
      }
      """
        .trimIndent() + "\n")

    assertEquals(expected, actual)
  }

  @Test
  fun freeform_and_octetstream_property_imports_and_types() {
    val obj =
      Model.Object(
        context = NamingContext.Named("Payloads"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "json",
              model = Model.FreeFormJson(description = null, constraint = null),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
            Model.Object.Property(
              baseName = "bytes",
              model = Model.OctetStream(description = null),
              isRequired = true,
              isNullable = false,
              description = null,
            ),
          ),
        inline = emptyList(),
      )

    val file = listOf<Model>(obj).toIrFile(pkg = "com.example")
    val actual = emitFile(file)

    val expected =
      ("""
      package com.example

      import kotlinx.serialization.json.JsonElement
      import kotlinx.serialization.Serializable

      @Serializable
      data class Payloads(val json: JsonElement, val bytes: ByteArray)
      """
        .trimIndent() + "\n")

    assertEquals(expected, actual)
  }
}
