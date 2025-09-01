package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.transform.ModelToIr.toIrFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ObjectNoRequireGoldenTest {
  @Test
  fun model_no_require_stubs_full_emission() {
    val model =
      Model.Object(
        context = NamingContext.Named("User"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "id",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = true,
              isNullable = false,
              description = null,
            )
          ),
        inline = emptyList(),
      )

    val file = listOf<Model>(model).toIrFile(pkg = "com.example")

    val expected =
      ("""
            package com.example

            data class User(val id: String)
            """
        .trimIndent() + "\n")

    assertEquals(expected, emitFile(file))
  }
}
