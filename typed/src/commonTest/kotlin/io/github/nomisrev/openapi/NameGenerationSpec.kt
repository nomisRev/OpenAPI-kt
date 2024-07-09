package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.assertThrows

/**
 * With any kind of disjunction, we need to generate names for the cases since we need to convert to
 * tagged unions.
 *
 * The tagged union name uses any naming information we have, so any nested cases needs to generate
 * a name explicitly.
 *
 * This concern is currently a bit clumsily defined in both the OpenAPITransformer, and the Model
 * generation.
 *
 * We currently have a couple special cases:
 * - Primitives result in `CaseInt`, `CaseString`, etc.
 * - With collections it becomes `CaseInts`, `CaseStrings`, etc.
 * - With complex types we use `ReferenceOr.Ref`, to avoid generating a name.
 * - For enums we concatenate the enum values, and capitalize the first letter. => AutoOrManual,
 *   AllOrNone, but this could result in very long names... -
 */
class UnionNameGenerationSpec {
  @Test
  fun nestedInlineObjInUnionGeneratesNameOnTypeProperty() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "TypeOrEvent" to
                    value(
                      Schema(
                        oneOf =
                          listOf(
                            value(
                              Schema(
                                type = Type.Basic.Object,
                                properties =
                                  mapOf(
                                    "type" to
                                      value(
                                        Schema(type = Type.Basic.String, enum = listOf("Function"))
                                      ),
                                    "value" to value(Schema(type = Type.Basic.String))
                                  )
                              )
                            ),
                            value(Schema(type = Type.Basic.Integer))
                          )
                      )
                    )
                )
            )
        )
        .models()
    val obj =
      Object(
        context =
          NamingContext.Nested(
            NamingContext.Named("Function"),
            NamingContext.Named("TypeOrEvent"),
          ),
        description = null,
        properties =
          listOf(
            Property(
              "type",
              Model.Enum.Closed(
                context =
                  NamingContext.Nested(
                    NamingContext.Named("type"),
                    NamingContext.Nested(
                      NamingContext.Named("Function"),
                      NamingContext.Named("TypeOrEvent"),
                    )
                  ),
                inner = Primitive.String(default = null, description = null),
                values = listOf("Function"),
                default = null,
                description = null
              ),
              isRequired = false,
              isNullable = true,
              description = null
            ),
            Property(
              "value",
              Primitive.String(default = null, description = null),
              isRequired = false,
              isNullable = true,
              description = null
            )
          ),
        inline =
          listOf(
            Model.Enum.Closed(
              context =
                NamingContext.Nested(
                  NamingContext.Named("type"),
                  NamingContext.Nested(
                    NamingContext.Named("Function"),
                    NamingContext.Named("TypeOrEvent"),
                  )
                ),
              inner = Primitive.String(default = null, description = null),
              values = listOf("Function"),
              default = null,
              description = null
            ),
            Primitive.String(default = null, description = null)
          )
      )
    val expected =
      Model.Union(
        context = NamingContext.Named("TypeOrEvent"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(
              context =
                NamingContext.Nested(
                  NamingContext.Named("Function"),
                  NamingContext.Named("TypeOrEvent"),
                ),
              model = obj
            ),
            Model.Union.Case(
              context = NamingContext.Named("TypeOrEvent"),
              model = Primitive.Int(default = null, description = null)
            )
          ),
        inline = listOf(obj, Primitive.Int(default = null, description = null))
      )

    assertEquals(setOf(expected), actual)
  }

  @Test
  fun nestedInlineObjInUnionGeneratesNameOnEventProperty() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "TypeOrEvent" to
                    value(
                      Schema(
                        oneOf =
                          listOf(
                            value(
                              Schema(
                                type = Type.Basic.Object,
                                properties =
                                  mapOf(
                                    "event" to
                                      value(
                                        Schema(type = Type.Basic.String, enum = listOf("RunThread"))
                                      ),
                                    "value" to value(Schema(type = Type.Basic.String))
                                  )
                              )
                            ),
                            value(Schema(type = Type.Basic.Integer))
                          )
                      )
                    )
                )
            )
        )
        .models()
    val obj =
      Object(
        context =
          NamingContext.Nested(
            NamingContext.Named("RunThread"),
            NamingContext.Named("TypeOrEvent"),
          ),
        description = null,
        properties =
          listOf(
            Property(
              "event",
              Model.Enum.Closed(
                context =
                  NamingContext.Nested(
                    NamingContext.Named("event"),
                    NamingContext.Nested(
                      NamingContext.Named("RunThread"),
                      NamingContext.Named("TypeOrEvent"),
                    )
                  ),
                inner = Primitive.String(default = null, description = null),
                values = listOf("RunThread"),
                default = null,
                description = null
              ),
              isRequired = false,
              isNullable = true,
              description = null
            ),
            Property(
              "value",
              Primitive.String(default = null, description = null),
              isRequired = false,
              isNullable = true,
              description = null
            )
          ),
        inline =
          listOf(
            Model.Enum.Closed(
              context =
                NamingContext.Nested(
                  NamingContext.Named("event"),
                  NamingContext.Nested(
                    NamingContext.Named("RunThread"),
                    NamingContext.Named("TypeOrEvent"),
                  )
                ),
              inner = Primitive.String(default = null, description = null),
              values = listOf("RunThread"),
              default = null,
              description = null
            ),
            Primitive.String(default = null, description = null)
          )
      )
    val expected =
      Model.Union(
        context = NamingContext.Named("TypeOrEvent"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(
              context =
                NamingContext.Nested(
                  NamingContext.Named("RunThread"),
                  NamingContext.Named("TypeOrEvent"),
                ),
              model = obj
            ),
            Model.Union.Case(
              context = NamingContext.Named("TypeOrEvent"),
              model = Primitive.Int(default = null, description = null)
            )
          ),
        inline = listOf(obj, Primitive.Int(default = null, description = null))
      )

    assertEquals(setOf(expected), actual)
  }

  @Test
  fun topLevelOneOfWithInlineEnumAndDefault() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "OneOf" to
                    value(
                      Schema(
                        oneOf = listOf(value(Schema(type = Type.Basic.String)), value(enumSchema))
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Union(
        context = NamingContext.Named("OneOf"),
        description = null,
        default = "Auto",
        cases =
          listOf(
            Model.Union.Case(
              context =
                NamingContext.Nested(
                  NamingContext.Named("AutoOrManual"),
                  NamingContext.Named("OneOf")
                ),
              model = enum
            ),
            // Order is important for deserialization,
            // order swapped compared to originally
            Model.Union.Case(
              context = NamingContext.Named("OneOf"),
              model = Primitive.String(default = null, description = null)
            )
          ),
        inline = listOf(Primitive.String(default = null, description = null), enum)
      )
    assertEquals(setOf(expected), actual)
  }

  @Test fun topLevelOneOfWithListAndInlineInner() = topLevelOneOfWithCollectionAndInlineInner(false)

  @Test fun topLevelOneOfWithSetAndInlineInner() = topLevelOneOfWithCollectionAndInlineInner(true)

  @Test
  fun topLevelOneOfWithCollectionAndInlineInner() = topLevelOneOfWithCollectionAndInlineInner(null)

  private fun topLevelOneOfWithCollectionAndInlineInner(uniqueItems: Boolean?) {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "OneOf" to
                    value(
                      Schema(
                        oneOf =
                          listOf(
                            value(
                              Schema(
                                type = Type.Basic.Array,
                                items = value(idSchema),
                                uniqueItems = uniqueItems
                              )
                            ),
                            value(Schema(type = Type.Basic.String)),
                          )
                      )
                    )
                )
            )
        )
        .models()
    val name = if (uniqueItems == true) "Set" else "List"
    val context = NamingContext.Nested(NamingContext.Named(name), NamingContext.Named("OneOf"))
    val id = id.copy(context = context)
    val model =
      if (uniqueItems == true) Model.Collection.Set(inner = id, default = null, description = null)
      else Model.Collection.List(inner = id, default = null, description = null)
    val expected =
      Model.Union(
        context = NamingContext.Named("OneOf"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(context = context, model = model),
            Model.Union.Case(
              context = NamingContext.Named("OneOf"),
              model = Primitive.String(default = null, description = null)
            )
          ),
        inline = listOf(id, Primitive.String(default = null, description = null))
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun nonSupportedCases() {
    assertThrows<NotImplementedError> {
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "TypeOrEvent" to
                    value(
                      Schema(
                        oneOf =
                          listOf(
                            value(
                              Schema(
                                type = Type.Basic.Object,
                                properties =
                                  mapOf("value" to value(Schema(type = Type.Basic.String)))
                              )
                            ),
                            value(Schema(type = Type.Basic.Integer))
                          )
                      )
                    )
                )
            )
        )
        .models()
    }
  }
}
