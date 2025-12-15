package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.render.render
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.reference
import kotlin.test.assertEquals

@TestRegistering
fun TestSuite.verify(
    expected: String,
    schema: Schema,
    expectedImports: Set<TypeName> = emptySet()
) {
    fun eq(expected: String, actual: String) =
        if (expected != actual) throw AssertionError(
            """
            |###################
            |###### Actual #####
            |$actual
            |###################
            |###### Expected ###
            |###################
            |$expected
            |###################
        """.trimMargin()
        )
        else Unit

    test(expected) {
        val model = registry(api.reference("Foo", schema)) {
            ReferenceOr.schema("Foo")
                .toModel(NamingContext.Reference("Foo", SchemaContext.Null), SchemaContext.Write)
        }
        val (actual, imports) = renderer { model.render() }
        eq(expected, actual)
        if (expectedImports.isNotEmpty()) {
            assertEquals(expectedImports, imports)
        }
    }
}


@TestRegistering
fun TestSuite.verify(
    expected: String,
    model: Model,
    expectedImports: Set<TypeName> = emptySet()
) {
    fun eq(expected: String, actual: String) =
        if (expected != actual) throw AssertionError(
            """
            |###################
            |###### Actual #####
            |$actual
            |###################
            |###### Expected ###
            |###################
            |$expected
            |###################
        """.trimMargin()
        )
        else Unit

    test(expected) {
        val (actual, imports) = renderer { model.render() }
        eq(expected, actual)
        if (expectedImports.isNotEmpty()) {
            assertEquals(expectedImports, imports)
        }
    }
}

val renderObjectSpec by testSuite {
    verify(
        """
        |@Serializable
        |data object Foo
        """.trimMargin(),
        Model.Object(
            NamingContext.reference("Foo", SchemaContext.Null),
            null,
            null,
            emptyList(),
            emptySet(),
            false,
            false
        )
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(@Required val value: String)""".trimMargin(),
        Model.Object.value(
            NamingContext.Reference("Foo", SchemaContext.Null),
            Model.Primitive.String(null, null, null, false, null)
        )
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(@Required val value: String)""".trimMargin(),
        Model.Object.value(
            NamingContext.Reference("Foo", SchemaContext.Null),
            Model.Primitive.String(null, null, null, true, null)
        )
    )

    val singline = Model.Object(
        context = NamingContext.reference("Foo", SchemaContext.Null),
        description = null,
        title = null,
        properties = listOf(
            Model.Object.Property("name", Model.Primitive.String(null, null, null, false, null), false),
            Model.Object.Property("email", Model.Primitive.Long(null, null, null, true, null), false),
            Model.Object.Property("age", Model.Primitive.Int(null, null, null, false, null), true),
            Model.Object.Property("longername", Model.Primitive.Double(null, null, null, true, null), true),
            Model.Object.Property("longername2", Model.Primitive.Float(null, null, null, false, null), false),
            Model.Object.Property("longer_name_3", Model.Uuid(null, false, null), false),
            Model.Object.Property("longername4", Model.DateTime(null, false, null), false),
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    verify(
        """|@Serializable
           |data class Foo(
           |    val name: String,
           |    val email: Long? = null,
           |    @Required val age: Int,
           |    @Required val longername: Double?,
           |    val longername2: Float,
           |    @SerialName("longer_name_3") val longerName3: Uuid,
           |    val longername4: LocalDateTime
           |)""".trimMargin(),
        singline
    )

    val enum = Model.Enum(
        NamingContext.reference("Sort", SchemaContext.Null),
        Model.Primitive.String(null, null, null, false, null),
        listOf("ASC", "DESC"),
        null,
        null,
        null,
        false
    )
    val nestedEnum = Model.Object.value(
        NamingContext.Reference("Foo", SchemaContext.Null),
        enum,
        inline = setOf(enum)
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(@Required val value: Sort) {
           |    @Serializable
           |    enum class Sort {
           |        ASC, DESC;
           |    }
           |}""".trimMargin(),
        nestedEnum
    )
}
