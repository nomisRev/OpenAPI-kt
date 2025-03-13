package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertFalse

class ModelTest {
  @Test
  fun dataClass() {
    val code =
      Model.Object(
          NamingContext.Named("User"),
          null,
          listOf(
            Model.Object.Property(
              "id",
              Model.Primitive.String(null, null, null),
              isRequired = true,
              isNullable = false,
              description = null,
            )
          ),
          listOf(Model.Primitive.String(null, null, null)),
        )
        .compiles()
    assertFalse(code.contains("requireAll"))
    assertFalse(code.contains("require"))
  }

  @Test
  fun union() {
    Model.Union(
        NamingContext.Named("IntOrString"),
        listOf(
          Model.Union.Case(
            NamingContext.Named("IntOrString"),
            Model.Primitive.Int(null, null, null),
          ),
          Model.Union.Case(
            NamingContext.Named("IntOrString"),
            Model.Primitive.String(null, null, null),
          ),
        ),
        null,
        null,
        listOf(Model.Primitive.String(null, null, null), Model.Primitive.String(null, null, null)),
      )
      .compiles()
  }
}
