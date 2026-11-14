package io.github.nomisrev.render

import io.github.nomisrev.openapi.EnumEntryNames
import io.github.nomisrev.openapi.enumEntryNames
import io.github.nomisrev.openapi.unescapeBackticks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** The entry names of one enum are chosen together: every value is an entry, whatever the order of the values. */
class EnumEntryNamesTest {
    private fun names(vararg values: String): List<String> =
        enumEntryNames(values.toList()).let { table -> values.map(table::nameOf) }

    private fun renames(vararg values: String): List<Pair<String, String>> =
        enumEntryNames(values.toList()).renames.map { it.wireValue to it.entryName }

    @Test
    fun anIllegalCharacterIsAWordBreak() {
        assertEquals(listOf("AwsKms"), names("aws:kms"))
        assertEquals(listOf("AudioL16Rate8000"), names("audio/l16;rate=8000"))
        assertEquals(listOf("XList"), names("x[]list"))
        assertEquals(listOf("AB"), names("a-b"))
    }

    @Test
    fun aValueWithoutLetterOrDigitIsSpelledOut() {
        assertEquals(
            listOf("LessThan", "LessThanOrEqual", "GreaterThan", "GreaterThanOrEqual", "Equals", "NotEquals"),
            names("<", "<=", ">", ">=", "=", "!="),
        )
        assertEquals(listOf("Plus", "Minus", "Dot", "Star", "Slash"), names("+", "-", ".", "*", "/"))
        assertEquals(listOf("Colon", "Semicolon", "Comma", "At", "Hash"), names(":", ";", ",", "@", "#"))
        assertEquals(listOf("MinusMinus", "DotDot"), names("--", ".."))
        assertEquals(listOf("U20AC"), names("€"))
        assertEquals(listOf("Empty", "Spiders"), names("", "spiders"))
        assertEquals(listOf("Blank"), names(" "))
        assertEquals(listOf("Blank", "Blank2"), names(" ", "  "))
    }

    @Test
    fun valuesWithOneNameSpellTheirSymbolsInEitherOrder() {
        assertEquals(listOf("LastActivity", "MinusLastActivity"), names("last_activity", "-last_activity"))
        assertEquals(listOf("MinusLastActivity", "LastActivity"), names("-last_activity", "last_activity"))
        assertEquals(listOf("AwsColonKms", "AwsMinusKms"), names("aws:kms", "aws-kms"))
        assertEquals(listOf("AwsMinusKms", "AwsColonKms"), names("aws-kms", "aws:kms"))
        assertEquals(
            listOf("LastActivity", "MinusLastActivity", "Digest", "MinusDigest"),
            names("last_activity", "-last_activity", "digest", "-digest"),
        )
        // The values of other names are not touched.
        assertEquals(listOf("Other", "LastActivity", "MinusLastActivity"), names("other", "last_activity", "-last_activity"))
    }

    @Test
    fun aValueThatKeepsItsNameIsNotReportedAsRenamed() {
        assertEquals(listOf("-last_activity" to "MinusLastActivity"), renames("last_activity", "-last_activity"))
        assertEquals(listOf("-last_activity" to "MinusLastActivity"), renames("-last_activity", "last_activity"))
        assertEquals(
            listOf("aws:kms" to "AwsColonKms", "aws-kms" to "AwsMinusKms"),
            renames("aws:kms", "aws-kms"),
        )
        assertEquals(emptyList(), renames("<", "<=", "", "a-b"))
    }

    @Test
    fun valuesThatStillShareANameTheFirstKeepsItAndTheLaterGetTheFirstFreeNumber() {
        assertEquals(listOf("Foo", "Foo2"), names("Foo", "foo"))
        assertEquals(listOf("Foo", "Foo2"), names("foo", "Foo"))
        assertEquals(listOf("Foo", "Foo2", "Foo3"), names("Foo", "foo", "foo_"))
        assertEquals(listOf("Empty", "Empty2"), names("", "Empty"))
        assertEquals(listOf("Empty", "Empty2"), names("Empty", ""))
        assertEquals(listOf("AMinusB", "AB", "AB2"), names("a-b", "a_b", "A B"))
        // A number that is a real value is not taken.
        assertEquals(listOf("Foo", "Foo2", "Foo3"), names("Foo", "Foo2", "foo"))
        assertEquals(listOf("foo" to "Foo3"), renames("Foo", "Foo2", "foo"))
        assertEquals(listOf("EAN13", "EAN132"), names("EAN_13", "EAN13"))
    }

    @Test
    fun aNameThatCompilesTodayIsKept() {
        assertEquals(listOf("`300`", "`600`", "`1200`"), names("300", "600", "1200"))
        assertEquals(listOf("`05`", "`15`"), names("0.5", "1.5"))
        assertEquals(listOf("`+1`", "`-1`"), names("+1", "-1"))
        assertEquals(listOf("`1xx`"), names("1xx"))
        assertEquals(listOf("Star", "Slash"), names("*", "/"))
    }

    @Test
    fun aTypedEnumTellsItsNumbersApart() {
        assertEquals(listOf("`0Dot5`", "Minus0Dot5"), names("0.5", "-0.5"))
        assertEquals(listOf("Minus0Dot5", "`0Dot5`"), names("-0.5", "0.5"))
        assertEquals(listOf("`1`", "`-1`"), names("1", "-1"))
    }

    @Test
    fun everyValueIsAnEntryWithAUniqueLegalName() {
        val values = listOf(
            "last_activity", "-last_activity", "aws:kms", "aws-kms", "audio/l16;rate=8000", "<", "<=", ">", ">=", "=",
            "!=", "", " ", ".", "..", "*", "/", "+", "-", "+1", "-1", "0.5", "-0.5", "Foo", "foo", "Empty", "a-b",
            "a_b", "A B", "[x]", "x[]", "a.b", "A.B", "a\tb", "\n", "café", "€", "1xx", "`", "a`b", "\\", "null",
        )
        for (ordered in listOf(values, values.reversed())) {
            val table: EnumEntryNames = enumEntryNames(ordered)
            val names = ordered.map(table::nameOf)
            assertEquals(ordered.size, names.map { it.unescapeBackticks() }.toSet().size, "unique: ${ordered.zip(names)}")
            for ([value, name] in ordered.zip(names)) {
                val bare = name.unescapeBackticks()
                assertTrue(bare.isNotBlank(), "'$value' has an empty name")
                assertTrue(bare.none { it in ".;[]/<>:\\`" || it.isWhitespace() || it.isISOControl() }, "'$value' -> $name")
                val identifier = Regex("[A-Za-z_][A-Za-z0-9_]*").matches(bare)
                assertEquals(if (identifier) bare else "`$bare`", name, "'$value' is backticked only when it must be")
            }
        }
    }
}
