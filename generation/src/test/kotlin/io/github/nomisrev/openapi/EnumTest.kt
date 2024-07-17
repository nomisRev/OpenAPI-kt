package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Named
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EnumTest {
  @Test
  fun enum() {
    val code =
      Model.Enum.Closed(
          Named("AutoOrManual"),
          Model.Primitive.String(null, null, null, false),
          listOf("Auto", "Manual"),
          "Auto",
          null
        )
        .compiles()
    assertFalse(code.contains("@SerialName(\"Auto\")"))
  }

  @Test
  fun enumNonValidClassNames() {
    val code =
      Model.Enum.Closed(
          Named("AutoOrManual"),
          Model.Primitive.String(null, null, null, false),
          listOf("auto", "manual"),
          "auto",
          null
        )
        .compiles()
    assertTrue(code.contains("@SerialName(\"auto\")"))
    assertTrue(code.contains("@SerialName(\"manual\")"))
  }

  @Test
  fun openEnum() {
    val code =
      Model.Enum.Open(Named("AutoOrManual"), listOf("auto", "manual"), "auto", null).compiles()
    assertTrue(code.contains("sealed interface AutoOrManual"))
    assertTrue(code.contains("data object Auto"))
    assertTrue(code.contains("data object Manual"))
    assertTrue(code.contains("value class OpenCase"))
  }
}
