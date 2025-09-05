package io.github.nomisrev.codegen.transform

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class PrimitiveEmissionGoldenTest {
  @Test
  fun top_level_primitives_should_not_emit_typealiases() {
    val models =
      listOf<Model>(
        Model.Primitive.String(default = null, description = null, constraint = null),
        Model.Primitive.Int(default = null, description = null, constraint = null),
        Model.Primitive.Boolean(default = null, description = null),
      )

    val file = models.toIrFile(pkg = "com.example")

    val expected =
      ("""
      package com.example
      """
        .trimIndent() + "\n\n")

    assertEquals(expected, emitFile(file))
  }

  @Test
  fun duplicate_primitives_should_not_emit_duplicate_aliases() {
    val models =
      listOf<Model>(
        Model.Primitive.String(default = null, description = null, constraint = null),
        Model.Primitive.String(default = null, description = null, constraint = null),
        Model.Primitive.Int(default = null, description = null, constraint = null),
        Model.Primitive.Int(default = null, description = null, constraint = null),
        Model.Primitive.Boolean(default = null, description = null),
        Model.Primitive.Boolean(default = null, description = null),
      )

    val file = models.toIrFile(pkg = "com.example")

    val expected =
      ("""
      package com.example
      """
        .trimIndent() + "\n\n")

    assertEquals(expected, emitFile(file))
  }

  @Test
  fun object_with_dollar_type_property_is_escaped_and_no_primitive_aliases() {
    val swimlane =
      Model.Object(
        context = NamingContext.Named("SwimlaneValue"),
        description = "Represents single swimlane in case of IssueBasedSwimlaneSettings.",
        properties =
          listOf(
            Model.Object.Property(
              baseName = "id",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
            Model.Object.Property(
              baseName = "name",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
            Model.Object.Property(
              baseName = "${'$'}type",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
          ),
        inline = emptyList(),
      )

    val file = listOf(swimlane).toIrFile(pkg = "com.example")

    val expected =
      ("""
      package com.example

      import kotlinx.serialization.Serializable

      /**
       * Represents single swimlane in case of IssueBasedSwimlaneSettings.
       */
      @Serializable
      data class SwimlaneValue(val id: String? = null, val name: String? = null, val `${'$'}type`: String? = null)
      """
        .trimIndent() + "\n")

    assertEquals(expected, emitFile(file))
  }
}
