package io.github.nomisrev.openapi

import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.fail

class YouTrackIntegrationTest {
  @Test
  fun youtrack_spec_routes_should_build() {
    // Try to locate the youtrack.json that exists at the repository root without duplicating it as
    // a resource.
    val candidates =
      listOf(
        Path("youtrack.json"), // when working directory is the repository root
        Path("../youtrack.json"), // when working directory is the module directory
      )
    val path =
      candidates.firstOrNull { it.exists() }
        ?: fail(
          "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
        )

    val json = path.readText()
    val openApi = OpenAPI.fromJson(json)

    val routes = openApi.routes()
    assertTrue(routes.isNotEmpty(), "Expected YouTrack spec to produce at least one route")
  }
}

class UnionSpecialNamingSpec {

  @Test
  fun nested_oneOf_with_allOf_derives_case_names_from_type_across_allOf_and_orders_by_complexity() {
    val creation =
      Schema(
        type = Schema.Type.Basic.Object,
        required = listOf("type"),
        properties =
          mapOf(
            "type" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.String, enum = listOf("creation")))
          ),
        description = ReferenceOr.Companion.value("Only allow users with bypass permission to create matching refs."),
      )

    val update =
      Schema(
        type = Schema.Type.Basic.Object,
        required = listOf("type", "parameters"),
        properties =
          mapOf(
            "type" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.String, enum = listOf("update"))),
            "parameters" to ReferenceOr.Companion.value(
                Schema(
                    type = Schema.Type.Basic.Object,
                    required = listOf("flag"),
                    properties = mapOf("flag" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Boolean))),
                )
            ),
          ),
        description = ReferenceOr.Companion.value("Only allow users with bypass permission to update matching refs."),
      )

    val rulesetInfo =
      Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("ruleset_id" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Integer)))
      )

    val detailed =
      Schema(
        type = Schema.Type.Basic.Object,
        oneOf =
          listOf(
              ReferenceOr.Companion.value(
                  Schema(
                      allOf = listOf(
                          ReferenceOr.Companion.schema("repository-rule-creation"),
                          ReferenceOr.Companion.schema("repository-rule-ruleset-info")
                      )
                  )
              ),
              ReferenceOr.Companion.value(
                  Schema(
                      allOf = listOf(
                          ReferenceOr.Companion.schema("repository-rule-update"),
                          ReferenceOr.Companion.schema("repository-rule-ruleset-info")
                      )
                  )
              ),
          ),
        description = ReferenceOr.Companion.value("A repository rule with ruleset details."),
      )

    val api =
      testAPI.withComponents(
        Components(
          schemas =
            mapOf(
              "repository-rule-creation" to ReferenceOr.Companion.value(creation),
              "repository-rule-update" to ReferenceOr.Companion.value(update),
              "repository-rule-ruleset-info" to ReferenceOr.Companion.value(rulesetInfo),
              "repository-rule-detailed" to ReferenceOr.Companion.value(detailed),
            )
        )
      )

    val union = api.models().first { m ->
      m is Model.Union && (m.context as? NamingContext.Named)?.name == "repository-rule-detailed"
    } as Model.Union

    // Most complex case (Update) should come first
    val case0 = union.cases[0]
    val inner0 = assertIs<NamingContext.Nested>(case0.context).inner
      assertEquals(NamingContext.Named("Update"), inner0)

    val case1 = union.cases[1]
    val inner1 = assertIs<NamingContext.Nested>(case1.context).inner
      assertEquals(NamingContext.Named("Creation"), inner1)
  }

  @Test
  fun discriminator_mapping_is_used_for_ref_cases() {
    val petCat =
      Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("meow" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Boolean)))
      )

    val petDog =
      Schema(
        type = Schema.Type.Basic.Object,
        properties =
          mapOf(
            "bark" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Boolean)),
            "age" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Integer)),
          )
      )

    val pet =
      Schema(
        oneOf = listOf(ReferenceOr.Companion.schema("PetCat"), ReferenceOr.Companion.schema("PetDog")),
        discriminator =
          Schema.Discriminator(
            propertyName = "kind",
            mapping = mapOf("Cat" to "#/components/schemas/PetCat", "Dog" to "PetDog"),
          ),
      )

    val api =
      testAPI.withComponents(
        Components(
          schemas = mapOf("PetCat" to ReferenceOr.Companion.value(petCat), "PetDog" to ReferenceOr.Companion.value(
              petDog
          ), "Pet" to ReferenceOr.Companion.value(pet)
          )
        )
      )

    val union = api.models().first { it is Model.Union && (it.context as? NamingContext.Named)?.name == "Pet" } as Model.Union

    // For $ref cases, context should be Named from discriminator.mapping keys
    val names = union.cases.map { (it.context as NamingContext.Named).name }.toSet()
      assertEquals(setOf("Cat", "Dog"), names)
  }

  @Test
  fun fallback_case_names_are_generated_when_no_discriminator_nor_special_props_found() {
    val unionSchema =
      Schema(
        oneOf =
          listOf(
              ReferenceOr.Companion.value(
                  Schema(
                      type = Schema.Type.Basic.Object,
                      properties = mapOf("a" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.String)))
                  )
              ),
              ReferenceOr.Companion.value(
                  Schema(
                      type = Schema.Type.Basic.Object,
                      properties =
                          mapOf(
                              "a" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.String)),
                              "b" to ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Integer)),
                          )
                  )
              ),
          ),
      )

    val api = testAPI.withComponents(Components(schemas = mapOf("Union" to ReferenceOr.Companion.value(unionSchema))))

    val union = api.models().first { it is Model.Union && (it.context as? NamingContext.Named)?.name == "Union" } as Model.Union

    // Names should fall back to <UnionName>Case{index+1} based on original order
    val caseNames = union.cases.map { assertIs<NamingContext.Nested>(it.context).inner }.map { (it as NamingContext.Named).name }.toSet()
      assertEquals(setOf("UnionCase1", "UnionCase2"), caseNames)

    // And the most complex case (2 properties) should be ordered first
    val firstInner = assertIs<NamingContext.Nested>(union.cases[0].context).inner
    assertTrue((firstInner as NamingContext.Named).name.endsWith("Case2"))
  }

  @Test
  fun enum_string_case_in_union_is_named_from_values() {
    val schema =
      Schema(
        oneOf =
          listOf(
              ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.String, enum = listOf("red", "blue"))),
              ReferenceOr.Companion.value(Schema(type = Schema.Type.Basic.Integer)),
          )
      )

    val api = testAPI.withComponents(Components(schemas = mapOf("ColorOrInt" to ReferenceOr.Companion.value(schema))))

    val union = api.models().first { it is Model.Union && (it.context as? NamingContext.Named)?.name == "ColorOrInt" } as Model.Union

    val firstCase = union.cases[0]
    val ctx = assertIs<NamingContext.Nested>(firstCase.context)
      assertEquals(NamingContext.Named("RedOrBlue"), ctx.inner)
      assertEquals(NamingContext.Named("ColorOrInt"), ctx.outer)

    val secondCase = union.cases[1]
      assertEquals(NamingContext.Named("ColorOrInt"), secondCase.context)
  }
}