package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext

val unionRenderSpec by testSuite {
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
            |}
        """.trimMargin(),
        Model.Union(
            context = NamingContext.Reference("Union", SchemaContext.Null),
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false), null),
                Model.Union.Case(Model.Primitive.Int(null, null, null, false), null),
            ),
            null,
            null,
            null,
            emptySet(),
            null,
            false
        )
    )

    val employeeCase = Model.Object(
        context = NamingContext.UnionCase,
        description = null,
        title = null,
        properties = listOf(
            Model.Object.Property("age", Model.Primitive.Int(null, null, null, false), false),
            Model.Object.Property("name", Model.Primitive.String(null, null, null, false), false),
        ),
        inline = emptySet(),
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
            context = NamingContext.Reference("Union", SchemaContext.Null),
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false), null),
                Model.Union.Case(employeeCase, null),
            ),
            null,
            null,
            null,
            setOf(employeeCase),
            null,
            false
        )
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
            context = NamingContext.Reference("Union", SchemaContext.Null),
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false), "reference"),
                Model.Union.Case(employeeCase, "employee"),
            ),
            null,
            null,
            null,
            setOf(employeeCase),
            $$"$type",
            false
        )
    )

    val aOrB = Model.Enum(
        context = NamingContext.UnionCase,
        inner = Model.Primitive.String(null, null, null, false),
        values = listOf("asc", "desc"),
        default = null,
        isOpen = false,
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
            context = NamingContext.Reference("Union", SchemaContext.Null),
            listOf(
                Model.Union.Case(Model.Primitive.String(null, null, null, false), null),
                Model.Union.Case(aOrB, null),
            ),
            null,
            null,
            null,
            setOf(aOrB),
            null,
            false
        )
    )
}
