package io.github.nomisrev.render

import io.github.nomisrev.render.test.union.name.collisions.Collisions
import io.github.nomisrev.render.test.union.name.collisions.Contact
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

/**
 * The cases of one union have their own class each, and decoding is deterministic. Uses the generated golden
 * `kotlinTestData/union/name-collisions`.
 */
class UnionCaseNameCollisionsRoundTripTest {
    private val json = Json

    private fun roundTrips(wire: String): Collisions {
        val decoded = json.decodeFromString<Collisions>(wire)
        assertEquals(wire, json.encodeToString(decoded))
        return decoded
    }

    @Test
    fun twoStringPatternsAreAString() {
        assertEquals(
            "las:transition:commons-x",
            roundTrips("""{"transitionId":"las:transition:commons-x"}""").transitionId,
        )
        assertEquals(
            "las:transition:0123456789abcdef0123456789abcdef",
            roundTrips("""{"transitionId":"las:transition:0123456789abcdef0123456789abcdef"}""").transitionId,
        )
        assertEquals("2020-01-01", roundTrips("""{"metadata":"2020-01-01"}""").metadata)
    }

    @Test
    fun aStringCaseNextToAnotherTypeDecodesEitherPattern() {
        assertEquals(Collisions.Mixed.CaseString("aaa"), roundTrips("""{"mixed":"aaa"}""").mixed)
        assertEquals(Collisions.Mixed.CaseString("bbb"), roundTrips("""{"mixed":"bbb"}""").mixed)
        assertEquals(Collisions.Mixed.CaseInt(5), roundTrips("""{"mixed":5}""").mixed)
    }

    @Test
    fun objectsToldApartByAnEnumValueAreTheirOwnClass() {
        val succeeded = roundTrips("""{"result":{"status":"succeeded"}}""").result
        assertEquals(Collisions.Result.Succeeded(Collisions.Result.Succeeded.Status.Succeeded), succeeded)

        val failed = roundTrips("""{"result":{"status":"failed"}}""").result
        assertEquals(Collisions.Result.FailedOrRejected(Collisions.Result.FailedOrRejected.Status.Failed), failed)
        val rejected = roundTrips("""{"result":{"status":"rejected"}}""").result
        assertEquals(Collisions.Result.FailedOrRejected(Collisions.Result.FailedOrRejected.Status.Rejected), rejected)
    }

    @Test
    fun objectsWithATitleKeepTheirTitle() {
        assertEquals(Collisions.Titled.First("x"), roundTrips("""{"titled":{"value":"x"}}""").titled)
        assertEquals(Collisions.Titled.Second(3), roundTrips("""{"titled":{"value":3}}""").titled)
    }

    @Test
    fun objectsNothingToldApartAreNumberedByPosition() {
        assertEquals(Collisions.Positional.Value1("x"), roundTrips("""{"positional":{"value":"x"}}""").positional)
        assertEquals(Collisions.Positional.Value2(3), roundTrips("""{"positional":{"value":3}}""").positional)
    }

    @Test
    fun structurallyIdenticalObjectsAreOneClass() {
        val accepted = roundTrips("""{"accepted":{"message_uuid":"123e4567-e89b-12d3-a456-426614174000"}}""").accepted
        assertIs<Collisions.Accepted>(accepted)
    }

    @Test
    fun requiredOnlyBranchesAreAConstraintOnTheObject() {
        val files = roundTrips("""{"files":{"a":{"content":"x"},"b":{"filename":"f"},"c":{},"d":null}}""").files!!
        assertEquals(Collisions.Files(content = "x"), files["a"])
        assertEquals(Collisions.Files(filename = "f"), files["b"])
        assertEquals(Collisions.Files(), files["c"])
        assertNull(files["d"])

        assertEquals(Contact(email = "a@b"), json.decodeFromString<Contact>("""{"email":"a@b"}"""))
        assertEquals(Contact(phone = "1"), json.decodeFromString<Contact>("""{"phone":"1"}"""))
        assertEquals(Contact(email = "a@b", phone = "1"), json.decodeFromString<Contact>("""{"email":"a@b","phone":"1"}"""))
    }
}
