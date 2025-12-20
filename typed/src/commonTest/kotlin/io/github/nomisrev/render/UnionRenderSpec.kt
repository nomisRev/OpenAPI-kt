package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext

val unionRenderSpec by testSuite {
    val union = NamingContext.reference("Union", SchemaContext.Null)
    verify(
        """
            |@Serializable
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseInt(val value: Int) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseFloat(val value: Float) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseDouble(val value: Double) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseLocalDate(val value: LocalDate) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseLocalDateTime(val value: LocalDateTime) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseByteArray(val value: ByteArray) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseUuid(val value: Uuid) : Union
            |    
            |    @Serializable
            |    data object Empty : Union
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.Float(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.Double(null, null, null, false, null), null),
                Model.Union.Case(Model.Date(null, false, null), null),
                Model.Union.Case(Model.DateTime(null, false, null), null),
                Model.Union.Case(Model.ByteArray(null, false, null), null),
                Model.Union.Case(Model.Uuid(null, false, null), null),
                Model.Union.Case(Model.Primitive.Unit(null, false, null), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.Uuid,
        TypeName.Date,
        TypeName.DateTime,
    )

    fun employeeCase(ctx: NamingContext.Nested) = Model.Object(
        context = NamingContext(NamingContext.Reference("Union", SchemaContext.Null), listOf(ctx)),
        description = null,
        title = null,
        properties = mapOf(
            "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, false, null), true),
            "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
        ),
        additionalProperties = false,
        isNullable = false
    )

    verify(
        """
            |@Serializable
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |    
            |    @Serializable
            |    data class Case1(val age: Int, val name: String) : Union
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(employeeCase(NamingContext.UnionCase("Case1")), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
    )

    verify(
        $$$"""
            |@JsonClassDiscriminator($$"$type")
            |@Serializable
            |sealed interface Union {
            |    @SerialName("reference")
            |    @Serializable
            |    @JvmInline
            |    value class Reference(val value: String) : Union
            |    
            |    @SerialName("employee")
            |    @Serializable
            |    data class Employee(val age: Int, val name: String) : Union
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), "reference"),
                Model.Union.Case(employeeCase(NamingContext.UnionCase("employee")), "employee"),
            ),
            null,
            null,
            null,
            $$"$type",
            false
        ),
        TypeName.JsonClassDiscriminator,
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.SerialName
    )

    val aOrB = Model.Enum(
        context = union.nest(NamingContext.UnionCase("AscOrDesc")),
        inner = Model.Primitive.String(null, null, null, false, null),
        values = listOf("asc", "desc"),
        default = null,
        description = null,
        title = null,
        isNullable = false
    )

    verify(
        """
            |@Serializable
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseString(val value: String) : Union
            |    
            |    @Serializable
            |    enum class AscOrDesc : Union {
            |        @SerialName("asc") Asc, @SerialName("desc") Desc;
            |    }
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                Model.Union.Case(aOrB, null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
        TypeName.SerialName
    )

    verify(
        """
            |@Serializable
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseStrings(val value: List<String>) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseInt(val value: Int) : Union
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(
                    Model.Collection(
                        Model.Primitive.String(null, null, null, false, null),
                        null,
                        null,
                        null,
                        false,
                        null
                    ),
                    null
                ),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline
    )

    verify(
        """
            |@Serializable
            |sealed interface Union {
            |    @Serializable
            |    @JvmInline
            |    value class CaseStringsList(val value: List<List<String>>) : Union
            |    
            |    @Serializable
            |    @JvmInline
            |    value class CaseInt(val value: Int) : Union
            |}
        """.trimMargin(),
        Model.Union(
            context = union,
            listOf(
                Model.Union.Case(
                    Model.Collection(
                        Model.Collection(
                            Model.Primitive.String(null, null, null, false, null),
                            null,
                            null,
                            null,
                            false,
                            null
                        ),
                        null,
                        null,
                        null,
                        false,
                        null
                    ),
                    null
                ),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
            ),
            null,
            null,
            null,
            null,
            false
        ),
        TypeName.Serializable,
        TypeName.JvmInline
    )

    verify(
        """
        |@Serializable
        |sealed interface Foo {
        |    @Serializable
        |    enum class FooOrBar : Foo {
        |        @SerialName("foo") Foo, @SerialName("bar") Bar;
        |    }
        |    
        |    @Serializable
        |    @JvmInline
        |    value class CaseString(val value: String) : Foo
        |}
        """.trimMargin(),
        Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema(type = Schema.Type.Basic.String, enum = listOf("foo", "bar"))),
                ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
            )
        )
    )
}
