package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals

class AnyValueSpec {

  @Test
  fun top_level_component_empty_schema_is_free_form_json() {
    val api =
      testAPI.copy(
        components = Components(schemas = mapOf("AnyValue" to value(Schema())))
      )

    val models = api.models()

    val expected = setOf(Model.FreeFormJson(description = null, constraint = null))
    assertEquals(expected, models)
  }

  @Test
  fun empty_schema_with_description_carries_over_description() {
    val api =
      testAPI.copy(
        components =
          Components(
            schemas =
              mapOf(
                "AnyValue" to value(Schema(description = value("Can be any value.")))
              )
          )
      )

    val models = api.models()

    val expected = setOf(Model.FreeFormJson(description = "Can be any value.", constraint = null))
    assertEquals(expected, models)
  }

  @Test
  fun array_items_anyvalue_is_list_of_free_form_json() {
    val schema = Schema(type = Type.Basic.Array, items = value(Schema()))
    val model = schema.toModel("AnyArray")

    val expected = Model.Collection.list(Model.FreeFormJson(null, null))
    assertEquals(expected, model)
  }

  @Test
  fun property_anyvalue_nullable_true_is_reflected_on_property() {
    val obj =
      Schema(
        type = Type.Basic.Object,
        properties = mapOf("any" to value(Schema(nullable = true))),
        required = emptyList(),
      )

    val model = obj.toModel("HasAny") as Model.Object

    // Property should be present, with isNullable = true and type FreeFormJson
    val expected =
      Model.obj(
        context = NamingContext.Named("HasAny"),
        properties =
          listOf(
            Model.Object.Property(
              baseName = "any",
              model = Model.FreeFormJson(null, null),
              isRequired = false,
              isNullable = true,
              description = null,
            )
          ),
        inline = listOf(Model.FreeFormJson(null, null)),
        description = null,
      )

    assertEquals(expected, model)
  }
}
