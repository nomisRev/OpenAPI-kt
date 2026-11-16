package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * A discriminator on a base schema whose subtypes are `allOf: [{$ref: Base}, ...]`, the polymorphism example of the
 * OpenAPI specification. The mapping lists the subtypes; it lists the base itself only when a plain base is a valid
 * payload under a tag of its own (YouTrack does, viator, twitter, microsoft graph, rebilly and just-eat do not).
 *
 * The tag only selects the schema: a value still validates against all of it, so a subtype has the properties of
 * every `allOf` member and of `properties` next to `allOf`, and of each parent up to the base. A subtype that is not
 * in the mapping is selected by its schema name (the implicit mapping, which explicit entries add to), and a mapped
 * schema that does not extend the base is still a case, with a warning.
 */
val discriminatorMappingWithoutBaseSpec by testSuite {
    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    fun document(schemas: Map<String, String>): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { ${schemas.entries.joinToString(",\n") { [name, schema] -> "\"$name\": $schema" }} } }
        }
        """.trimIndent()

    fun base(properties: String, vararg mapping: Pair<String, String>, required: String = """["type"]"""): String = """
        {
          "type": "object",
          "required": $required,
          "properties": { "type": { "type": "string" }, $properties },
          "discriminator": {
            "propertyName": "type",
            "mapping": { ${mapping.joinToString(", ") { [tag, schema] -> "\"$tag\": \"#/components/schemas/$schema\"" }} }
          }
        }
        """.trimIndent()

    fun extends(parent: String, properties: String = "", required: String = ""): String =
        """{ "allOf": [${ref(parent)}, { "type": "object", "properties": { $properties }$required }] }"""

    suspend fun discriminated(name: String, schemas: Map<String, String>): Pair<Model.DiscriminatedObject, List<String>> =
        Registry(OpenAPI.fromJson(document(schemas))).use { registry ->
            val model = with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)
            }
            assertIs<Model.DiscriminatedObject>(model) to registry.warnings()
        }

    fun Model.DiscriminatedObject.tags(): List<String> =
        subtypes.map { (it.context.nested.last() as NamingContext.DiscriminatedObjectCase).discriminator }

    fun Model.DiscriminatedObject.case(tag: String): Model.Object = subtypes.single {
        (it.context.nested.last() as NamingContext.DiscriminatedObjectCase).discriminator == tag
    }

    test("a mapping without the base itself has the mapped subtypes as its only cases") {
        val [pet, warnings] = discriminated(
            "Pet",
            mapOf(
                "Pet" to base(""""name": { "type": "string" }""", "cat" to "Cat", "dog" to "Dog"),
                "Cat" to extends("Pet", """"huntingSkill": { "type": "string" }"""),
                "Dog" to extends("Pet", """"packSize": { "type": "integer" }"""),
            ),
        )
        assertEquals(listOf("cat", "dog"), pet.tags())
        assertEquals(setOf("name"), pet.abstractProperties.keys)
        assertEquals(setOf("name", "huntingSkill"), pet.case("cat").properties.keys)
        assertEquals(setOf("name", "packSize"), pet.case("dog").properties.keys)
        assertEquals(emptyList(), warnings)
    }

    test("a subtype with only the base in its allOf is a case with the base's properties") {
        val [problem, _] = discriminated(
            "Problem",
            mapOf(
                "Problem" to base(""""title": { "type": "string" }""", "generic" to "Generic"),
                "Generic" to """{ "allOf": [${ref("Problem")}] }""",
            ),
        )
        assertEquals(listOf("generic"), problem.tags())
        assertEquals(setOf("title"), problem.case("generic").properties.keys)
    }

    test("a subtype of a mapped intermediate parent has the properties of every parent up to the base") {
        val [entity, _] = discriminated(
            "Entity",
            mapOf(
                "Entity" to base(""""id": { "type": "string" }""", "directoryObject" to "DirectoryObject", "user" to "User"),
                "DirectoryObject" to extends("Entity", """"deletedDateTime": { "type": "string" }"""),
                "User" to extends("DirectoryObject", """"mail": { "type": "string" }"""),
            ),
        )
        assertEquals(listOf("directoryObject", "user"), entity.tags())
        assertEquals(setOf("id", "deletedDateTime"), entity.case("directoryObject").properties.keys)
        assertEquals(setOf("id", "deletedDateTime", "mail"), entity.case("user").properties.keys)
    }

    test("a subtype has the properties of every allOf member next to the base, and requires what they require") {
        val [restriction, _] = discriminated(
            "Restriction",
            mapOf(
                // The base maps itself here, so the merge is checked on its own (a mapping without it crashed earlier).
                "Restriction" to base(
                    """"id": { "type": "string" }""",
                    "restriction" to "Restriction",
                    "per-redemption" to "PerRedemption",
                ),
                "Limit" to """{ "type": "object", "required": ["limit"], "properties": { "limit": { "type": "integer" } } }""",
                "PerRedemption" to """
                    { "allOf": [
                      ${ref("Restriction")},
                      ${ref("Limit")},
                      { "type": "object", "required": ["quantity"], "properties": { "quantity": { "type": "integer" } } }
                    ] }
                """.trimIndent(),
            ),
        )
        val case = restriction.case("per-redemption")
        assertEquals(setOf("id", "limit", "quantity"), case.properties.keys)
        assertEquals(setOf("limit", "quantity"), case.properties.filterValues { it.isRequired }.keys)
    }

    test("a subtype has the properties declared next to its allOf") {
        val [issue, _] = discriminated(
            "Issue",
            mapOf(
                "Issue" to base(""""code": { "type": "string" }""", "issue" to "Issue", "missing" to "Missing"),
                "Missing" to """
                    {
                      "allOf": [${ref("Issue")}, { "type": "object", "additionalProperties": false }],
                      "properties": { "detail": { "type": "string" } }
                    }
                """.trimIndent(),
            ),
        )
        val case = issue.case("missing")
        assertEquals(setOf("code", "detail"), case.properties.keys)
        assertEquals(Model.Object.AdditionalProperties.Allowed(false), case.additionalProperties)
    }

    test("a subtype the mapping does not list is a case under its schema name, the implicit mapping") {
        val [pet, warnings] = discriminated(
            "Pet",
            mapOf(
                "Pet" to base(""""name": { "type": "string" }""", "pet" to "Pet", "cat" to "Cat"),
                "Cat" to extends("Pet", """"huntingSkill": { "type": "string" }"""),
                "Dog" to extends("Pet", """"packSize": { "type": "integer" }"""),
                "Puppy" to extends("Dog", """"age": { "type": "integer" }"""),
            ),
        )
        assertEquals(listOf("pet", "cat", "Dog", "Puppy"), pet.tags())
        assertEquals(setOf("name", "packSize", "age"), pet.case("Puppy").properties.keys)
        assertEquals(emptyList(), warnings)
    }

    test("the base in its own mapping is still the case of a plain base") {
        val [pet, _] = discriminated(
            "Pet",
            mapOf(
                "Pet" to base(""""name": { "type": "string" }""", "pet" to "Pet", "cat" to "Cat"),
                "Cat" to extends("Pet", """"huntingSkill": { "type": "string" }"""),
            ),
        )
        assertEquals(listOf("pet", "cat"), pet.tags())
    }

    test("a mapped schema that does not extend the base is a case with its own properties, with a warning") {
        val [pet, warnings] = discriminated(
            "Pet",
            mapOf(
                "Pet" to base(""""name": { "type": "string" }""", "cat" to "Cat", "robot" to "Robot"),
                "Cat" to extends("Pet", """"huntingSkill": { "type": "string" }"""),
                "Machine" to """{ "type": "object", "properties": { "serial": { "type": "string" } } }""",
                "Robot" to """{ "allOf": [${ref("Machine")}] }""",
            ),
        )
        assertEquals(listOf("cat", "robot"), pet.tags())
        assertEquals(setOf("serial"), pet.case("robot").properties.keys)
        assertEquals(
            listOf(
                "Discriminator of 'Pet': the mapping 'robot' selects 'Robot', which does not extend 'Pet' through " +
                    "allOf; it is generated as a case with its own properties only."
            ),
            warnings,
        )
    }

    suspend fun referenced(schemas: Map<String, String>): NamingContext =
        Registry(OpenAPI.fromJson(document(schemas + ("Holder" to """
            { "type": "object", "properties": { "pet": ${ref("Target")} } }
        """.trimIndent())))).use { registry ->
            val holder = with(registry) {
                ReferenceOr.schema("Holder").toModel(NamingContext.reference("Holder", SchemaContext.Null), SchemaContext.Write)
            }
            assertIs<Model.Reference>(assertIs<Model.Object>(holder).properties.getValue("pet").model).context
        }

    fun case(base: String, tag: String): NamingContext =
        NamingContext.reference(base, SchemaContext.Write).nest(NamingContext.DiscriminatedObjectCase(tag))

    test("a \$ref to a subtype is the case its tag selects, also when the tag is not the schema name") {
        val schemas = mapOf(
            "Pet" to base(""""name": { "type": "string" }""", "pet" to "Pet", "feline" to "Target"),
            "Target" to extends("Pet", """"huntingSkill": { "type": "string" }"""),
        )
        assertEquals(case("Pet", "feline"), referenced(schemas))
        assertEquals(
            case("Pet", "feline"),
            referenced(schemas + ("Pet" to base(""""name": { "type": "string" }""", "feline" to "Target"))),
        )
    }

    test("a \$ref to a subtype the mapping does not list is the case of its schema name") {
        assertEquals(
            case("Pet", "Target"),
            referenced(
                mapOf(
                    "Pet" to base(""""name": { "type": "string" }""", "cat" to "Cat"),
                    "Cat" to extends("Pet"),
                    "Target" to extends("Pet", """"packSize": { "type": "integer" }"""),
                )
            ),
        )
    }

    test("a \$ref to a subtype with several allOf members is the case of the base it extends") {
        assertEquals(
            case("Restriction", "per-redemption"),
            referenced(
                mapOf(
                    "Restriction" to base(""""id": { "type": "string" }""", "per-redemption" to "Target"),
                    "Limit" to """{ "type": "object", "properties": { "limit": { "type": "integer" } } }""",
                    "Target" to """{ "allOf": [${ref("Restriction")}, ${ref("Limit")}, { "type": "object" }] }""",
                )
            ),
        )
    }
}
