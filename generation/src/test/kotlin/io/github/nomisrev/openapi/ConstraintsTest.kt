package io.github.nomisrev.openapi

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class ConstraintsTest {
  private fun prop(name: String, type: Model) =
    Model.Object.Property(name, type, isRequired = true, isNullable = false, description = null)

  private val id =
    prop(
      "id",
      Model.Primitive.String(
        null,
        null,
        Constraints.Text(maxLength = 10, minLength = 1, pattern = null),
        false
      )
    )
  private val idRequirements =
    listOf("id.length in 1..10", "id should have a length between 1 and 10")

  private val age =
    prop(
      "age",
      Model.Primitive.Int(
        null,
        null,
        Constraints.Number(
          maximum = 100.0,
          minimum = 0.0,
          exclusiveMaximum = false,
          exclusiveMinimum = false,
          multipleOf = null
        )
      )
    )
  private val ageRequirements =
    listOf(
      "age in 0..100",
      "age should be larger or equal to 0 and should be smaller or equal to 100"
    )

  private val height =
    prop(
      "height",
      Model.Primitive.Double(
        null,
        "Height in cm",
        Constraints.Number(
          maximum = 300.0,
          minimum = 30.0,
          exclusiveMaximum = false,
          exclusiveMinimum = false,
          multipleOf = null
        )
      )
    )
  private val heightRequirements =
    listOf(
      "30.0 <= height && height <= 300.0",
      "height should be larger or equal to 30.0 and should be smaller or equal to 300.0"
    )

  private val tags =
    prop(
      "tags",
      Model.Collection.List(
        Model.Primitive.String(null, null, null, false),
        null,
        null,
        Constraints.Collection(minItems = 3, maxItems = 10)
      )
    )
  private val tagsRequirements =
    listOf("tags.size in 3..10", "tags should have between 3 and 10 elements")

  private val categories =
    prop(
      "categories",
      Model.Collection.Set(
        Model.Primitive.String(null, null, null, false),
        null,
        null,
        Constraints.Collection(minItems = 3, maxItems = 10)
      )
    )
  private val categoriesRequirements =
    listOf("categories.size in 3..10", "categories should have between 3 and 10 elements")

  @Test
  fun dataClassSingleTextConstraints() {
    val code =
      Model.Object(NamingContext.Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(idRequirements))
  }

  @Test
  fun dataClassSingleIntConstraint() {
    val code =
      Model.Object(NamingContext.Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(ageRequirements))
  }

  @Test
  fun dataClassSingleListConstraint() {
    val code =
      Model.Object(NamingContext.Named("User"), null, listOf(tags), listOf(tags.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(tagsRequirements))
  }

  @Test
  fun dataClassSingleSetConstraint() {
    val code =
      Model.Object(NamingContext.Named("User"), null, listOf(categories), listOf(categories.model))
        .compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(categoriesRequirements))
  }

  @Test
  fun dataClassSingleDoubleConstraint() {
    val code =
      Model.Object(NamingContext.Named("User"), null, listOf(height), listOf(height.model))
        .compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(heightRequirements))
  }

  @Test
  fun dataClassAllConstraints() {
    val code =
      Model.Object(
          NamingContext.Named("User"),
          null,
          listOf(id, age, height),
          listOf(id.model, age.model, height.model)
        )
        .compiles()
    assertTrue(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(idRequirements))
    assertTrue(code.containsSingle(ageRequirements))
    assertTrue(code.containsSingle(heightRequirements))
  }
}
