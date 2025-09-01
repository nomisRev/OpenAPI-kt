package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.transform.ModelToIr.toIrFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ClosedEnumGoldenTest {
    @Test
    fun enum_lowercase_values_have_SerialName_annotations() {
        val model = Model.Enum.Closed(
            context = NamingContext.Named("AutoOrManual"),
            inner = Model.Primitive.String(default = null, description = null, constraint = null),
            values = listOf("auto", "manual"),
            default = "auto",
            description = null,
        )

        val file = listOf<Model>(model).toIrFile(pkg = "com.example")

        val expected = (
            """
            package com.example

            import kotlinx.serialization.SerialName

            enum class AutoOrManual {
                @SerialName("auto")
                Auto,
                @SerialName("manual")
                Manual
            }
            """.trimIndent() + "\n"
        )

        assertEquals(expected, emitFile(file))
    }
}
