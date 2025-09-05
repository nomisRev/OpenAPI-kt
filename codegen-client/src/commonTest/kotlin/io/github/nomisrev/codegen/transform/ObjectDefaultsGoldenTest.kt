package io.github.nomisrev.codegen.transform

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectDefaultsGoldenTest {
  @Test
  fun defaults_and_type_names_for_free_form_json_and_map() {
    val payload =
      Model.Object(
        context = NamingContext.Named("Payload"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "json",
              model = Model.FreeFormJson(description = null, constraint = null),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
            Model.Object.Property(
              baseName = "attributes",
              model =
                Model.Collection.Map(
                  inner =
                    Model.Primitive.Int(default = null, description = null, constraint = null),
                  description = null,
                  constraint = null,
                ),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
          ),
        inline = emptyList(),
      )

    val file = listOf<Model>(payload).toIrFile(pkg = "com.example")

    val expected =
      ("""
            package com.example

            import kotlinx.serialization.json.JsonElement
            import kotlinx.serialization.Serializable

            @Serializable
            data class Payload(val json: JsonElement? = null, val attributes: Map<String, Int>? = null)
            """
        .trimIndent() + "\n")

    assertEquals(expected, emitFile(file))
  }
}
