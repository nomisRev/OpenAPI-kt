package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Constraints.Collection
import io.github.nomisrev.openapi.Constraints.Number
import io.github.nomisrev.openapi.NamingContext.Named
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class DataClassConstraintsTest {
  private fun prop(name: String, type: Model, isNullable: Boolean = false) =
    Model.Object.Property(
      name,
      type,
      isRequired = true,
      isNullable = isNullable,
      description = null
    )

  private fun id(constraint: Constraints.Text) =
    prop("id", Model.Primitive.String(null, null, constraint, false))

  val id = id(Constraints.Text(1, 10, null))

  private val idRequirements =
    listOf("id.length in 1..10", "id should have a length between 1 and 10")

  private val patternRequirements =
    listOf(
      "id.matches(\"\"\"[a-zA-Z0-9]+\"\"\".toRegex()))",
      "id should match the pattern [a-zA-Z0-9]+"
    )

  private val age =
    age(
      Number(
        maximum = 100.0,
        minimum = 0.0,
        exclusiveMaximum = false,
        exclusiveMinimum = false,
        multipleOf = null
      )
    )

  private fun age(constraints: Number) = prop("age", Model.Primitive.Int(null, null, constraints))

  private val ageRequirements =
    listOf(
      "age in 0..100",
      "age should be larger or equal to 0 and should be smaller or equal to 100"
    )

  private fun height(constraints: Number) =
    prop("height", Model.Primitive.Double(null, "Height in cm", constraints))

  private val height =
    height(
      Number(
        maximum = 300.0,
        minimum = 30.0,
        exclusiveMaximum = false,
        exclusiveMinimum = false,
        multipleOf = null
      )
    )

  private val heightRequirements =
    listOf(
      "30.0 <= height && height <= 300.0",
      "height should be larger or equal to 30.0 and should be smaller or equal to 300.0"
    )

  private fun tags(constraints: Collection) =
    prop(
      "tags",
      Model.Collection.List(
        Model.Primitive.String(null, null, null, false),
        null,
        null,
        constraints
      )
    )

  private val tags = tags(Collection(minItems = 3, maxItems = 10))

  private val tagsRequirements =
    listOf("tags.size in 3..10", "tags should have between 3 and 10 elements")

  private fun categories(constraints: Collection) =
    prop(
      "categories",
      Model.Collection.Set(Model.Primitive.String(null, null, null, false), null, null, constraints)
    )

  private val categories = categories(Collection(minItems = 3, maxItems = 10))

  private val categoriesRequirements =
    listOf("categories.size in 3..10", "categories should have between 3 and 10 elements")

  @Test
  fun textMinAndMax() {
    val code = Model.Object(Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(idRequirements))
  }

  @Test
  fun textMin() {
    val id =
      prop(
        "id",
        Model.Primitive.String(null, null, Constraints.Text(1, Int.MAX_VALUE, null), false)
      )
    val code = Model.Object(Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("id.length >= 1"))
    assertTrue(code.containsSingle("id should have a length of at least 1"))
  }

  @Test
  fun singlePropNullable() {
    val id =
      prop(
        "id",
        Model.Primitive.String(null, null, Constraints.Text(1, Int.MAX_VALUE, null), false),
        isNullable = true
      )
    val code = Model.Object(Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("if (id != null)"))
    assertTrue(code.containsSingle("id.length >= 1"))
    assertTrue(code.containsSingle("id should have a length of at least 1"))
  }

  @Test
  fun textMax() {
    val id = prop("id", Model.Primitive.String(null, null, Constraints.Text(0, 100, null), false))
    val code = Model.Object(Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("id.length <= 100"))
    assertTrue(code.containsSingle("id should have a length of at most 100"))
  }

  @Test
  fun textPattern() {
    val id =
      prop(
        "id",
        Model.Primitive.String(
          null,
          null,
          Constraints.Text(0, Int.MAX_VALUE, "[a-zA-Z0-9]+"),
          false
        )
      )
    val code = Model.Object(Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(patternRequirements))
  }

  @Test
  fun complexTextConstraints() {
    val id = id(Constraints.Text(minLength = 1, maxLength = 10, pattern = "[a-zA-Z0-9]+"))
    val code = Model.Object(Named("User"), null, listOf(id), listOf(id.model)).compiles()
    assertTrue(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(idRequirements + patternRequirements))
  }

  @Test
  fun intOpenClosedMinMaxRange() {
    val age = age(Number(false, 0.0, true, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("age in 0..<100"))
    assertTrue(
      code.containsSingle("age should be larger or equal to 0 and should be smaller then 100")
    )
  }

  @Test
  fun intClosedMinMaxRange() {
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(ageRequirements))
  }

  @Test
  fun intMin() {
    val age = age(Number(false, 0.0, false, Double.POSITIVE_INFINITY, null))
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0 <= age"))
    assertTrue(code.containsSingle("age should be larger or equal to 0"))
  }

  @Test
  fun intMax() {
    val age = age(Number(false, Double.NEGATIVE_INFINITY, false, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("age <= 100"))
    assertTrue(code.containsSingle("age should be smaller or equal to 100"))
  }

  @Test
  fun intOpenMinMax() {
    val age = age(Number(true, Double.NEGATIVE_INFINITY, true, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("age < 100"))
    assertTrue(code.containsSingle("age should be smaller then 100"))
  }

  @Test
  fun intOpenMinClosedMax() {
    val age = age(Number(true, Double.NEGATIVE_INFINITY, false, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("age <= 100"))
    assertTrue(code.containsSingle("age should be smaller or equal to 100"))
  }

  @Test
  fun intClosedMinOpenMax() {
    val age = age(Number(false, Double.NEGATIVE_INFINITY, true, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(age), listOf(age.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("age < 100"))
    assertTrue(code.containsSingle("age should be smaller then 100"))
  }

  @Test
  fun listMinMax() {
    val code = Model.Object(Named("User"), null, listOf(tags), listOf(tags.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(tagsRequirements))
  }

  @Test
  fun listMin() {
    val tags = tags(Collection(3, Int.MAX_VALUE))
    val code = Model.Object(Named("User"), null, listOf(tags), listOf(tags.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("tags.size >= 3"))
    assertTrue(code.containsSingle("tags should have at least 3 elements"))
  }

  @Test
  fun listMax() {
    val tags = tags(Collection(0, 100))
    val code = Model.Object(Named("User"), null, listOf(tags), listOf(tags.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("tags.size <= 100"))
    assertTrue(code.containsSingle("tags should have at most 100 elements"))
  }

  @Test
  fun setMinMax() {
    val code =
      Model.Object(Named("User"), null, listOf(categories), listOf(categories.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(categoriesRequirements))
  }

  @Test
  fun setMin() {
    val categories = categories(Collection(3, Int.MAX_VALUE))
    val code =
      Model.Object(Named("User"), null, listOf(categories), listOf(categories.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("categories.size >= 3"))
    assertTrue(code.containsSingle("categories should have at least 3 elements"))
  }

  @Test
  fun setMax() {
    val categories = categories(Collection(0, 100))
    val code =
      Model.Object(Named("User"), null, listOf(categories), listOf(categories.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("categories.size <= 100"))
    assertTrue(code.containsSingle("categories should have at most 100 elements"))
  }

  @Test
  fun `double min lte max lte`() {
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(heightRequirements))
  }

  @Test
  fun `double min lt max lte`() {
    val height = height(Number(true, 0.0, false, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 < height && height <= 100.0"))
    assertTrue(
      code.containsSingle(
        "height should be larger then 0.0 and should be smaller or equal to 100.0"
      )
    )
  }

  @Test
  fun `double min lte max lt`() {
    val height = height(Number(false, 0.0, true, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 <= height && height < 100.0"))
    assertTrue(
      code.containsSingle(
        "height should be larger or equal to 0.0 and should be smaller then 100.0"
      )
    )
  }

  @Test
  fun `double min lt max lt`() {
    val height = height(Number(true, 0.0, true, 100.0, null))
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 < height && height < 100.0"))
    assertTrue(
      code.containsSingle("height should be larger then 0.0 and should be smaller then 100.0")
    )
  }

  @Test
  fun `double min lt (max open)`() {
    val height =
      height(
        Number(
          exclusiveMinimum = true,
          minimum = 0.0,
          exclusiveMaximum = false,
          maximum = Double.POSITIVE_INFINITY,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 < height"))
    assertTrue(code.containsSingle("height should be larger then 0.0"))
  }

  @Test
  fun `double min lte (max open)`() {
    val height =
      height(
        Number(
          exclusiveMinimum = false,
          minimum = 0.0,
          exclusiveMaximum = false,
          maximum = Double.POSITIVE_INFINITY,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 <= height"))
    assertTrue(code.containsSingle("height should be larger or equal to 0.0"))
  }

  @Test
  fun `double min lt (max closed)`() {
    val height =
      height(
        Number(
          exclusiveMinimum = true,
          minimum = 0.0,
          exclusiveMaximum = true,
          maximum = Double.POSITIVE_INFINITY,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 < height"))
    assertTrue(code.containsSingle("height should be larger then 0.0"))
  }

  @Test
  fun `double min lte (max closed)`() {
    val height =
      height(
        Number(
          exclusiveMinimum = false,
          minimum = 0.0,
          exclusiveMaximum = true,
          maximum = Double.POSITIVE_INFINITY,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("0.0 <= height"))
    assertTrue(code.containsSingle("height should be larger or equal to 0.0"))
  }

  @Test
  fun `double (min closed) max lt`() {
    val height =
      height(
        Number(
          exclusiveMinimum = true,
          minimum = Double.NEGATIVE_INFINITY,
          exclusiveMaximum = true,
          maximum = 100.0,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("height < 100.0"))
    assertTrue(code.containsSingle("height should be smaller then 100.0"))
  }

  @Test
  fun `double (min open) max lt`() {
    val height =
      height(
        Number(
          exclusiveMinimum = false,
          minimum = Double.NEGATIVE_INFINITY,
          exclusiveMaximum = true,
          maximum = 100.0,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("height < 100.0"))
    assertTrue(code.containsSingle("height should be smaller then 100.0"))
  }

  @Test
  fun `double (min open) max lte`() {
    val height =
      height(
        Number(
          exclusiveMinimum = false,
          minimum = Double.NEGATIVE_INFINITY,
          exclusiveMaximum = false,
          maximum = 300.0,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("height <= 300.0"))
    assertTrue(code.containsSingle("height should be smaller or equal to 300.0"))
  }

  @Test
  fun `double (min closed) max lte`() {
    val height =
      height(
        Number(
          exclusiveMinimum = true,
          minimum = Double.NEGATIVE_INFINITY,
          exclusiveMaximum = false,
          maximum = 100.0,
          multipleOf = null
        )
      )
    val code = Model.Object(Named("User"), null, listOf(height), listOf(height.model)).compiles()
    assertFalse(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle("height <= 100.0"))
    assertTrue(code.containsSingle("height should be smaller or equal to 100.0"))
  }

  @Test
  fun allConstraints() {
    val nullable = prop("tags", tags.model, isNullable = true)
    val code =
      Model.Object(
          Named("User"),
          null,
          listOf(id, age, height, nullable),
          listOf(id.model, age.model, height.model, nullable.model)
        )
        .compiles()
    assertTrue(code.containsSingle("requireAll"))
    assertTrue(code.containsSingle(idRequirements))
    assertTrue(code.containsSingle(ageRequirements))
    assertTrue(code.containsSingle(heightRequirements))
    assertTrue(code.containsSingle("if (tags != null)"))
    assertTrue(code.containsSingle(tagsRequirements))
  }

  @Test
  fun password() {
    val prop = prop("password", Model.Primitive.String(null, null, null, true))
    val code = Model.Object(Named("User"), null, listOf(prop), listOf(prop.model)).compiles()
    assertTrue(code.containsSingle("val password: Password"))
  }
}
