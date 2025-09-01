package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.NamingContext.Named
import io.github.nomisrev.openapi.NamingContext.Nested
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals

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
      Schema(
          oneOf =
            listOf(
              value(
                Schema(
                  type = Type.Basic.Object,
                  properties =
                    mapOf(
                      "type" to value(Schema(type = Type.Basic.String, enum = listOf("Function"))),
                      "value" to value(Schema(type = Type.Basic.String)),
                    ),
                )
              ),
              value(Schema(type = Type.Basic.Integer)),
            )
        )
        .toModel("TypeOrEvent")
    val context = Nested(Named("Function"), Named("TypeOrEvent"))
    val propContext = Nested(Named("type"), context)
    val obj =
      Model.obj(
        context = context,
        properties =
          listOf(
            Object.property(
              "type",
              Model.Enum.Closed(
                context = propContext,
                inner = Primitive.string(),
                values = listOf("Function"),
                default = null,
                description = null,
              ),
            ),
            Object.property("value", Primitive.string()),
          ),
        inline =
          listOf(
            Model.Enum.Closed(
              context = propContext,
              inner = Primitive.string(),
              values = listOf("Function"),
              default = null,
              description = null,
            ),
            Primitive.string(),
          ),
      )
    val expected =
      Model.Union(
        context = Named("TypeOrEvent"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(context = context, model = obj),
            Model.Union.Case(context = Named("TypeOrEvent"), model = Primitive.int()),
          ),
        inline = listOf(obj, Primitive.int()),
        discriminator = null,
      )

    assertEquals(expected, actual)
  }

  @Test
  fun nestedInlineObjInUnionGeneratesNameOnEventProperty() {
    val actual =
      Schema(
          oneOf =
            listOf(
              value(
                Schema(
                  type = Type.Basic.Object,
                  properties =
                    mapOf(
                      "event" to
                        value(Schema(type = Type.Basic.String, enum = listOf("RunThread"))),
                      "value" to value(Schema(type = Type.Basic.String)),
                    ),
                )
              ),
              value(Schema(type = Type.Basic.Integer)),
            )
        )
        .toModel("TypeOrEvent")
    val obj =
      Model.obj(
        context = Nested(Named("RunThread"), Named("TypeOrEvent")),
        properties =
          listOf(
            Property(
              "event",
              Model.Enum.Closed(
                context = Nested(Named("event"), Nested(Named("RunThread"), Named("TypeOrEvent"))),
                inner = Primitive.string(),
                values = listOf("RunThread"),
                default = null,
                description = null,
              ),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
            Property(
              "value",
              Primitive.string(),
              isRequired = false,
              isNullable = true,
              description = null,
            ),
          ),
        inline =
          listOf(
            Model.Enum.Closed(
              context = Nested(Named("event"), Nested(Named("RunThread"), Named("TypeOrEvent"))),
              inner = Primitive.string(),
              values = listOf("RunThread"),
              default = null,
              description = null,
            ),
            Primitive.string(),
          ),
      )
    val expected =
      Model.Union(
        context = Named("TypeOrEvent"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(
              context = Nested(Named("RunThread"), Named("TypeOrEvent")),
              model = obj,
            ),
            Model.Union.Case(context = Named("TypeOrEvent"), model = Primitive.int()),
          ),
        inline = listOf(obj, Primitive.int()),
        discriminator = null,
      )

    assertEquals(expected, actual)
  }

  @Test
  fun topLevelOneOfWithInlineEnumAndDefault() {
    val actual =
      Schema(oneOf = listOf(value(Schema(type = Type.Basic.String)), value(enumSchema)))
        .toModel("OneOf")
    val expected =
      Model.Union(
        context = Named("OneOf"),
        description = null,
        default = "Auto",
        cases =
          listOf(
            Model.Union.Case(context = Nested(Named("AutoOrManual"), Named("OneOf")), model = enum),
            // Order is important for deserialization,
            // order swapped compared to originally
            Model.Union.Case(context = Named("OneOf"), model = Primitive.string()),
          ),
        inline = listOf(Primitive.string(), enum),
        discriminator = null,
      )
    assertEquals(expected, actual)
  }

  @Test fun topLevelOneOfWithListAndInlineInner() = topLevelOneOfWithCollectionAndInlineInner(false)

  @Test fun topLevelOneOfWithSetAndInlineInner() = topLevelOneOfWithCollectionAndInlineInner(true)

  @Test
  fun topLevelOneOfWithCollectionAndInlineInner() = topLevelOneOfWithCollectionAndInlineInner(null)

  private fun topLevelOneOfWithCollectionAndInlineInner(uniqueItems: Boolean?) {
    val actual =
      Schema(
          oneOf =
            listOf(
              value(
                Schema(type = Type.Basic.Array, items = value(idSchema), uniqueItems = uniqueItems)
              ),
              value(Schema(type = Type.Basic.String)),
            )
        )
        .toModel("OneOf")
    val name = if (uniqueItems == true) "Set" else "List"
    val context = Nested(Named(name), Named("OneOf"))
    val id = id.copy(context = context)
    val model =
      if (uniqueItems == true) Model.Collection.set(inner = id, default = null, description = null)
      else Model.Collection.list(inner = id, default = null, description = null)
    val expected =
      Model.Union(
        context = Named("OneOf"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(context = context, model = model),
            Model.Union.Case(context = Named("OneOf"), model = Primitive.string()),
          ),
        inline = listOf(id, Primitive.string()),
        discriminator = null,
      )
    assertEquals(expected, actual)
  }

  @Test
  fun nonSupportedCases() {
    val actual =
      Schema(
          oneOf =
            listOf(
              value(
                Schema(
                  type = Type.Basic.Object,
                  properties = mapOf("value" to value(Schema(type = Type.Basic.String))),
                )
              ),
              value(Schema(type = Type.Basic.Integer)),
            )
        )
        .toModel("TypeOrEvent")

    val expected =
      Model.Union(
        context = Named("TypeOrEvent"),
        description = null,
        default = null,
        cases =
          listOf(
            Model.Union.Case(
              context = Nested(Named("TypeOrEventCase1"), Named("TypeOrEvent")),
              model =
                Model.obj(
                  context = Nested(Named("TypeOrEventCase1"), Named("TypeOrEvent")),
                  properties =
                    listOf(
                      Property(
                        "value",
                        Primitive.string(),
                        isRequired = false,
                        isNullable = true,
                        description = null,
                      )
                    ),
                  inline = listOf(Primitive.string()),
                ),
            ),
            Model.Union.Case(context = Named("TypeOrEvent"), model = Primitive.int()),
          ),
        inline =
          listOf(
            Model.obj(
              context = Nested(Named("TypeOrEventCase1"), Named("TypeOrEvent")),
              properties =
                listOf(
                  Property(
                    "value",
                    Primitive.string(),
                    isRequired = false,
                    isNullable = true,
                    description = null,
                  )
                ),
              inline = listOf(Primitive.string()),
            ),
            Primitive.int(),
          ),
        discriminator = null,
      )

    assertEquals(expected, actual)
  }
}
