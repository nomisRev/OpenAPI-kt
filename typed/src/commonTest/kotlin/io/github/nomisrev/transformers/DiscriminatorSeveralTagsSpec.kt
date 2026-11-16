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
import kotlin.test.assertTrue

/**
 * A discriminator mapping is a map from tag to schema, and nothing requires its schemas to be distinct: one schema can
 * be selected by several tags (apple.com:sirikit-cloud-media selects one handler's schema by each of its JSON-RPC
 * `method` names). Such a schema is one case, which keeps the discriminator property as an enum of its tags, so that a
 * value is written with the tag it was read with; a case of a single tag has no such property.
 */
val discriminatorSeveralTagsSpec by testSuite {
    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    fun document(schemas: Map<String, String>): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { ${schemas.entries.joinToString(",\n") { [name, schema] -> "\"$name\": $schema" }} } }
        }
        """.trimIndent()

    fun base(vararg mapping: Pair<String, String>, discriminator: String = "type", extra: String = ""): String = """
        {
          "type": "object",
          "required": ["$discriminator"],
          "properties": { "$discriminator": { "type": "string" }, "name": { "type": "string" } }$extra,
          "discriminator": {
            "propertyName": "$discriminator",
            "mapping": { ${mapping.joinToString(", ") { [tag, schema] -> "\"$tag\": \"#/components/schemas/$schema\"" }} }
          }
        }
        """.trimIndent()

    fun extends(parent: String): String = """{ "allOf": [${ref(parent)}] }"""

    suspend fun model(name: String, schemas: Map<String, String>): Model =
        Registry(OpenAPI.fromJson(document(schemas))).use { registry ->
            with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)
            }
        }

    fun Model.Object.caseName(): String = (context.nested.last() as NamingContext.DiscriminatedObjectCase).discriminator

    fun Model.DiscriminatedObject.case(name: String): Model.Object = subtypes.single { it.caseName() == name }

    fun Model.Object.Property.enumValues(): List<String> = assertIs<Model.Enum>(model).values.map { it.toString() }

    test("a subtype the mapping selects under several tags is one case, whose tag is an enum of them") {
        val pet = assertIs<Model.DiscriminatedObject>(
            model(
                "Pet",
                mapOf(
                    "Pet" to base("cat" to "Cat", "kitten" to "Cat", "dog" to "Dog"),
                    "Cat" to extends("Pet"),
                    "Dog" to extends("Pet"),
                ),
            )
        )
        assertEquals(listOf("Cat", "dog"), pet.subtypes.map { it.caseName() })
        assertEquals(mapOf("Cat" to listOf("cat", "kitten")), pet.caseTags)
        val type = pet.case("Cat").properties.getValue("type")
        assertTrue(type.isRequired)
        assertEquals(listOf("cat", "kitten"), type.enumValues())
        assertEquals(setOf("name"), pet.case("dog").properties.keys)
    }

    test("the base under several tags is one case of a plain base, whose tag is an enum of them") {
        val pet = assertIs<Model.DiscriminatedObject>(
            model(
                "Pet",
                mapOf(
                    "Pet" to base("pet" to "Pet", "animal" to "Pet", "cat" to "Cat"),
                    "Cat" to extends("Pet"),
                ),
            )
        )
        assertEquals(listOf("Default", "cat"), pet.subtypes.map { it.caseName() })
        assertEquals(mapOf("Default" to listOf("pet", "animal")), pet.caseTags)
        assertEquals(listOf("pet", "animal"), pet.case("Default").properties.getValue("type").enumValues())
    }

    test("the enum of a case's tags replaces the discriminator property it declares") {
        val pet = assertIs<Model.DiscriminatedObject>(
            model(
                "Pet",
                mapOf(
                    "Pet" to base("cat" to "Cat", "kitten" to "Cat"),
                    "Cat" to """
                        { "allOf": [${ref("Pet")}, { "type": "object", "properties": {
                          "type": { "type": "string", "enum": ["kitten", "cat", "tiger"] } } }] }
                    """.trimIndent(),
                ),
            )
        )
        assertEquals(listOf("cat", "kitten"), pet.case("Cat").properties.getValue("type").enumValues())
    }

    suspend fun referenced(target: String, schemas: Map<String, String>): NamingContext {
        val holder = model(
            "Holder",
            schemas + ("Holder" to """{ "type": "object", "properties": { "value": ${ref(target)} } }"""),
        )
        return assertIs<Model.Reference>(assertIs<Model.Object>(holder).properties.getValue("value").model).context
    }

    fun case(base: String, name: String): NamingContext =
        NamingContext.reference(base, SchemaContext.Write).nest(NamingContext.DiscriminatedObjectCase(name))

    test("a \$ref to a subtype under several tags is its one case") {
        assertEquals(
            case("Pet", "Cat"),
            referenced(
                "Cat",
                mapOf("Pet" to base("cat" to "Cat", "kitten" to "Cat"), "Cat" to extends("Pet")),
            ),
        )
    }

    // apple.com:sirikit-cloud-media: each handler's schema extends `Invocation` and repeats the part of the mapping
    // that selects it, in a discriminator of its own.
    val sirikit = mapOf(
        "Invocation" to base(
            "AddMedia.confirm" to "AddMediaInvocation",
            "AddMedia.handle" to "AddMediaInvocation",
            "PlayMedia.handle" to "PlayMediaInvocation",
            discriminator = "method",
        ),
        "AddMediaInvocation" to """
            {
              "allOf": [${ref("Invocation")}],
              "properties": { "method": { "type": "string", "enum": ["AddMedia.confirm", "AddMedia.handle"] } },
              "discriminator": { "propertyName": "method", "mapping": {
                "AddMedia.confirm": "#/components/schemas/AddMediaInvocation",
                "AddMedia.handle": "#/components/schemas/AddMediaInvocation" } }
            }
        """.trimIndent(),
        "PlayMediaInvocation" to extends("Invocation"),
    )

    test("a schema with a discriminator of its own that extends a discriminated base is a case of that base") {
        val invocation = assertIs<Model.DiscriminatedObject>(model("Invocation", sirikit))
        assertEquals(listOf("AddMediaInvocation", "PlayMedia.handle"), invocation.subtypes.map { it.caseName() })
        assertEquals(mapOf("AddMediaInvocation" to listOf("AddMedia.confirm", "AddMedia.handle")), invocation.caseTags)
        assertEquals(case("Invocation", "AddMediaInvocation"), referenced("AddMediaInvocation", sirikit))
    }

    // sirikit's subtypes also declare `type: object` next to their `allOf`, which adds nothing to the `$ref` member.
    test("a subtype that declares type object next to its allOf is a subtype") {
        val typed = sirikit.mapValues { [name, schema] ->
            if (name == "Invocation") schema else schema.replaceFirst("{", """{ "type": "object", """)
        }
        val invocation = assertIs<Model.DiscriminatedObject>(model("Invocation", typed))
        assertEquals(listOf("AddMediaInvocation", "PlayMedia.handle"), invocation.subtypes.map { it.caseName() })
        assertEquals(case("Invocation", "AddMediaInvocation"), referenced("AddMediaInvocation", typed))
    }
}
