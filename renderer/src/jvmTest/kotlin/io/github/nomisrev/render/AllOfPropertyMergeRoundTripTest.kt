package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.`property`.merge.Dog
import io.github.nomisrev.render.test.client.allof.`property`.merge.MyString
import io.github.nomisrev.render.test.client.allof.`property`.merge.NullableOwner
import io.github.nomisrev.render.test.client.allof.`property`.merge.PropertyMerge
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Instant

/**
 * A property that several `allOf` members declare accepts only values every declaration accepts: `null` only where
 * every declaration permits it, only the shared enum values, a date-time, and the more specific `Dog` with its own
 * properties. Uses the generated goldens under `kotlinTestData/client/allof-property-merge` directly.
 */
class AllOfPropertyMergeRoundTripTest {
    private val json = Json

    private val valid = """
        {
          "nullableThenNonNull": "a",
          "nonNullThenNullable": "b",
          "bothNullable": null,
          "createdAt": "2024-01-02T03:04:05Z",
          "status": "inactive",
          "pet": { "id": 1, "name": "Rex", "bark": true },
          "owner": null,
          "name": "n",
          "ownerWithExtra": { "id": 2, "extra": "x" }
        }
    """.trimIndent()

    @Test
    fun decodesAndEncodesTheIntersection() {
        val decoded = json.decodeFromString<PropertyMerge>(valid)
        assertEquals(
            PropertyMerge(
                nullableThenNonNull = "a",
                nonNullThenNullable = "b",
                bothNullable = null,
                createdAt = Instant.parse("2024-01-02T03:04:05Z"),
                status = PropertyMerge.Status.Inactive,
                pet = Dog(1, "Rex", true),
                owner = null,
                name = MyString("n"),
                ownerWithExtra = PropertyMerge.OwnerWithExtra(2, extra = "x"),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(valid), json.parseToJsonElement(json.encodeToString(decoded)))
        val withOwner = valid.replace(""""owner": null""", """"owner": { "id": 3 }""")
        assertEquals(NullableOwner(3), json.decodeFromString<PropertyMerge>(withOwner).owner)
    }

    @Test
    fun rejectsValuesOneDeclarationExcludes() {
        listOf(
            valid.replace(""""nullableThenNonNull": "a"""", """"nullableThenNonNull": null"""),
            valid.replace(""""nonNullThenNullable": "b"""", """"nonNullThenNullable": null"""),
            valid.replace(""""status": "inactive"""", """"status": null"""),
            valid.replace(""""status": "inactive"""", """"status": "banned""""),
            valid.replace(""""createdAt": "2024-01-02T03:04:05Z"""", """"createdAt": "yesterday""""),
        ).forEach { invalid ->
            // SerializationException, or IllegalArgumentException for a malformed Instant.
            assertFailsWith<IllegalArgumentException>(invalid) { json.decodeFromString<PropertyMerge>(invalid) }
        }
    }
}
