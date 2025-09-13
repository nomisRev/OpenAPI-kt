package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class RecursiveRefYamlTest {
  @Test
  fun parsesRecursiveRefInYaml() {
    val yaml = resourceText("recursive-ref.yml")
    val openAPI = OpenAPI.fromYaml(yaml)

    val compoundFilterRefOr = openAPI.components.schemas["CompoundFilter"]
    assertNotNull(compoundFilterRefOr, "CompoundFilter schema is missing")
    val compoundFilter = assertIs<ReferenceOr.Value<Schema>>(compoundFilterRefOr).value

    val filters = compoundFilter.properties?.get("filters")
    assertNotNull(filters, "CompoundFilter.properties.filters missing")
    val filtersSchema = assertIs<ReferenceOr.Value<Schema>>(filters).value

    val items = filtersSchema.items
    assertNotNull(items, "filters.items missing")
    val itemsSchema = assertIs<ReferenceOr.Value<Schema>>(items).value

    val anyOf = itemsSchema.anyOf
    assertNotNull(anyOf, "filters.items.anyOf missing")

    val refs = anyOf.map { assertIs<ReferenceOr.Reference>(it) }.map { it.ref }.toSet()

    assertEquals(
      setOf("#/components/schemas/ComparisonFilter", "#"),
      refs,
      "anyOf should contain a components ref and a recursive ref '#'"
    )
  }
}
