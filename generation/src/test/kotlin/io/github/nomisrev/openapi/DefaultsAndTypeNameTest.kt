package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Named
import kotlin.test.Test
import kotlin.test.assertTrue

class DefaultsAndTypeNameTest {

  @Test
  fun collection_empty_list_default_and_map_and_special_types() {
    val payload =
      Model.Object(
        Named("Payload"),
        null,
        listOf(
          Model.Object.Property(
            "files",
            Model.Collection.List(Model.OctetStream(null), emptyList(), null, null),
            isRequired = false,
            isNullable = true,
            description = null,
          ),
          Model.Object.Property(
            "json",
            Model.FreeFormJson(null, null),
            isRequired = false,
            isNullable = true,
            description = null,
          ),
          Model.Object.Property(
            "attributes",
            Model.Collection.Map(Model.Primitive.Int(null, null, null), null, null),
            isRequired = false,
            isNullable = true,
            description = null,
          ),
        ),
        inline = listOf(Model.OctetStream(null), Model.Primitive.Int(null, null, null)),
      )

    val code = payload.compiles()

    // Empty list default for collection should be rendered as emptyList()
    assertTrue(code.contains("files: List<UploadFile>? = emptyList()"), code)
    // Free form JSON should map to JsonElement
    assertTrue(code.contains("json: JsonElement? = null"), code)
    // Map should map to Map<String, Int>
    assertTrue(code.contains("attributes: Map<String, Int>? = null"), code)
  }
}
