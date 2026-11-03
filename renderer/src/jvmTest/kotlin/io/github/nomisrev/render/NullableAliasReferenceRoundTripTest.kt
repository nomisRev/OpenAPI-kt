package io.github.nomisrev.render

import io.github.nomisrev.render.test.object_.nullable.alias.reference.AllOfNullableAlias
import io.github.nomisrev.render.test.object_.nullable.alias.reference.AnyOfAlias
import io.github.nomisrev.render.test.object_.nullable.alias.reference.NullFirstAnyOfAlias
import io.github.nomisrev.render.test.object_.nullable.alias.reference.NullableAlias
import io.github.nomisrev.render.test.object_.nullable.alias.reference.NullableAliasHolder
import io.github.nomisrev.render.test.object_.nullable.alias.reference.OneOfAlias
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A `$ref` to a named nullable alias (`anyOf`/`oneOf: [X, {type: null}]`, `nullable: true`,
 * `allOf: [{$ref: X}], nullable: true`) accepts both a value and `null` on the wire.
 * Uses the generated goldens under `kotlinTestData/object/nullable-alias-reference` directly.
 */
class NullableAliasReferenceRoundTripTest {
    private val json = Json

    private val allValues = """
        {
          "anyOfAlias": "x",
          "nullFirstAnyOfAlias": "n",
          "oneOfAlias": "o",
          "nullableAlias": "a",
          "allOfNullableAlias": { "name": "simon" },
          "optionalAnyOfAlias": "opt",
          "anyOfAliases": ["x", null],
          "anyOfAliasMap": { "a": "x", "b": null }
        }
    """.trimIndent()

    private val values = NullableAliasHolder(
        anyOfAlias = AnyOfAlias("x"),
        nullFirstAnyOfAlias = NullFirstAnyOfAlias("n"),
        oneOfAlias = OneOfAlias("o"),
        nullableAlias = NullableAlias("a"),
        allOfNullableAlias = AllOfNullableAlias("simon"),
        optionalAnyOfAlias = AnyOfAlias("opt"),
        anyOfAliases = listOf(AnyOfAlias("x"), null),
        anyOfAliasMap = mapOf("a" to AnyOfAlias("x"), "b" to null),
    )

    @Test
    fun everyAliasDecodesItsValue() {
        val decoded = json.decodeFromString<NullableAliasHolder>(allValues)
        assertEquals(values, decoded)
        assertEquals(decoded, json.decodeFromString<NullableAliasHolder>(json.encodeToString(decoded)))
    }

    @Test
    fun everyAliasDecodesNull() {
        val nullKeys = listOf(
            "anyOfAlias", "nullFirstAnyOfAlias", "oneOfAlias", "nullableAlias", "allOfNullableAlias", "optionalAnyOfAlias",
        )
        val withNulls = JsonObject(json.parseToJsonElement(allValues).jsonObject + nullKeys.associateWith { JsonNull })
        val decoded = json.decodeFromJsonElement(NullableAliasHolder.serializer(), withNulls)
        assertEquals(
            values.copy(
                anyOfAlias = null,
                nullFirstAnyOfAlias = null,
                oneOfAlias = null,
                nullableAlias = null,
                allOfNullableAlias = null,
                optionalAnyOfAlias = null,
            ),
            decoded
        )
    }

    @Test
    fun requiredAnyOfAliasAcceptsNullAndValue() {
        val rest = """"nullFirstAnyOfAlias":null,"oneOfAlias":null,"nullableAlias":null,"allOfNullableAlias":null,"anyOfAliases":[],"anyOfAliasMap":{}"""
        assertEquals(null, json.decodeFromString<NullableAliasHolder>("""{"anyOfAlias":null,$rest}""").anyOfAlias)
        assertEquals(AnyOfAlias("x"), json.decodeFromString<NullableAliasHolder>("""{"anyOfAlias":"x",$rest}""").anyOfAlias)
    }
}
