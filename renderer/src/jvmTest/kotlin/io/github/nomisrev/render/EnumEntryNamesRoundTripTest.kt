package io.github.nomisrev.render

import io.github.nomisrev.render.test.`enum`.entry.names.Encryption
import io.github.nomisrev.render.test.`enum`.entry.names.Kind
import io.github.nomisrev.render.test.`enum`.entry.names.Level
import io.github.nomisrev.render.test.`enum`.entry.names.Listing
import io.github.nomisrev.render.test.`enum`.entry.names.Operator
import io.github.nomisrev.render.test.`enum`.entry.names.Ordering
import io.github.nomisrev.render.test.`enum`.entry.names.Sort
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Two values of one enum that have one Kotlin name, values that are only symbols and an empty value are each an entry
 * that goes over the wire as exactly its own text and is read back as itself (golden `kotlinTestData/enum/entry-names`).
 */
class EnumEntryNamesRoundTripTest {
    private val json = Json

    private inline fun <reified A> assertRoundTrip(entry: A, wire: String) {
        assertEquals("\"$wire\"", json.encodeToString(entry))
        assertEquals(entry, json.decodeFromString<A>("\"$wire\""))
    }

    @Test
    fun valuesThatDifferOnlyByASignAreTwoEntries() {
        assertRoundTrip(Ordering.LastActivity, "last_activity")
        assertRoundTrip(Ordering.MinusLastActivity, "-last_activity")
        assertEquals(listOf("last_activity", "-last_activity"), Ordering.entries.map { it.value })
    }

    @Test
    fun valuesThatAreOnlySymbolsAreEntriesToo() {
        assertRoundTrip(Operator.Eq, "eq")
        assertRoundTrip(Operator.LessThan, "<")
        assertRoundTrip(Operator.LessThanOrEqual, "<=")
        assertRoundTrip(Operator.GreaterThan, ">")
        assertRoundTrip(Operator.GreaterThanOrEqual, ">=")
        assertEquals(5, Operator.entries.size)
    }

    @Test
    fun anIllegalCharacterTellsTwoValuesApart() {
        assertRoundTrip(Encryption.AwsColonKms, "aws:kms")
        assertRoundTrip(Encryption.AwsMinusKms, "aws-kms")
    }

    @Test
    fun theEmptyValueIsAnEntry() {
        assertRoundTrip(Kind.Empty, "")
        assertRoundTrip(Kind.Spiders, "spiders")
    }

    @Test
    fun aNumberEnumTellsItsValuesApart() {
        assertEquals("0.5", json.encodeToString(Level.`0Dot5`))
        assertEquals("-0.5", json.encodeToString(Level.Minus0Dot5))
        assertEquals(Level.`0Dot5`, json.decodeFromString<Level>("0.5"))
        assertEquals(Level.Minus0Dot5, json.decodeFromString<Level>("-0.5"))
    }

    @Test
    fun anOpenEnumKeepsBothValuesAndUnknownOnes() {
        assertRoundTrip<Sort>(Sort.LastActivityOrLastActivity.LastActivity, "last_activity")
        assertRoundTrip<Sort>(Sort.LastActivityOrLastActivity.MinusLastActivity, "-last_activity")
        assertRoundTrip<Sort>(Sort.CaseString("other"), "other")
    }

    @Test
    fun anEnumPropertyRoundTrips() {
        val listing = Listing(
            ordering = Ordering.MinusLastActivity,
            operator = Operator.GreaterThanOrEqual,
            kind = Kind.Empty,
            direction = Listing.Direction.LastActivity,
            operators = listOf(Listing.Operators.LessThan, Listing.Operators.GreaterThan),
        )
        val wire = """{"ordering":"-last_activity","operator":">=","kind":"","direction":"last_activity","operators":["<",">"]}"""
        assertEquals(wire, json.encodeToString(listing))
        assertEquals(listing, json.decodeFromString<Listing>(wire))
    }
}
