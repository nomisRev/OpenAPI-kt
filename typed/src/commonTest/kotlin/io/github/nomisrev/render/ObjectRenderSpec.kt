package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.TypeName
import io.github.nomisrev.openapi.routes.SchemaContext

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
            emptyMap(),
            emptySet(),
            false,
            false
        ),
        TypeName.Serializable,
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(val value: String)""".trimMargin(),
        Model.Object.value(
            NamingContext.Reference("Foo", SchemaContext.Null),
            Model.Primitive.String(null, null, null, false, null)
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(val value: String)""".trimMargin(),
        Model.Object.value(
            NamingContext.Reference("Foo", SchemaContext.Null),
            Model.Primitive.String(null, null, null, true, null)
        ),
        TypeName.Serializable,
        TypeName.JvmInline,
    )

    val singleline = Model.Object(
        context = NamingContext.reference("Foo", SchemaContext.Null),
        description = null,
        title = null,
        properties = mapOf(
            "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
            "email" to Model.Object.Property(Model.Primitive.Long(null, null, null, true, null), false),
            "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, false, null), true),
            "longername" to Model.Object.Property(Model.Primitive.Double(null, null, null, true, null), true),
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    verify(
        """|@Serializable
           |data class Foo(val name: String? = null, val email: Long? = null, val age: Int, val longername: Double?)
           """.trimMargin(),
        singleline,
        TypeName.Serializable,
    )

    val multiline = Model.Object(
        context = NamingContext.reference("Foo", SchemaContext.Null),
        description = null,
        title = null,
        properties = mapOf(
            "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
            "email" to Model.Object.Property(Model.Primitive.Long(null, null, null, true, null), false),
            "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, false, null), true),
            "longername" to Model.Object.Property(Model.Primitive.Double(null, null, null, true, null), true),
            "longername2" to Model.Object.Property(Model.Primitive.Float(null, null, null, false, null), false),
            "longer_name_3" to Model.Object.Property(Model.Uuid(null, false, null), false),
            "longername4" to Model.Object.Property(Model.DateTime(null, false, null), false),
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    verify(
        """|@Serializable
           |data class Foo(
           |    val name: String? = null,
           |    val email: Long? = null,
           |    val age: Int,
           |    val longername: Double?,
           |    val longername2: Float? = null,
           |    @SerialName("longer_name_3") val longerName3: Uuid? = null,
           |    val longername4: LocalDateTime? = null
           |)""".trimMargin(),
        multiline,
        TypeName.Serializable,
        TypeName.SerialName,
        TypeName.Uuid,
        TypeName.DateTime,
    )

    val enum = Model.Enum(
        NamingContext.reference("Foo", SchemaContext.Null)
            .nest(NamingContext.ObjectProperty("Sort")),
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
           |value class Foo(val value: Sort) {
           |    @Serializable
           |    enum class Sort {
           |        ASC, DESC;
           |    }
           |}""".trimMargin(),
        nestedEnum,
        TypeName.Serializable,
        TypeName.JvmInline,
    )

    val primitiveImports = Model.Object(
        context = NamingContext.reference("Foo", SchemaContext.Null),
        description = null,
        title = null,
        properties = mapOf(
            "date" to Model.Object.Property(Model.Date(null, false, null), false),
            "dateTime" to Model.Object.Property(Model.DateTime(null, false, null), false),
            "uuid" to Model.Object.Property(Model.Uuid(null, false, null), false),
            "json" to Model.Object.Property(Model.FreeFormJson(null, null, false, null), false),
            "jsonArray" to Model.Object.Property(
                Model.Collection(
                    Model.FreeFormJson(null, null, false, null), null, null, null, false, null
                ), false
            ),
            "jsonObject" to Model.Object.Property(
                Model.Object(
                    NamingContext.reference("Foo", SchemaContext.Null)
                        .nest(NamingContext.ObjectProperty("jsonObject")),
                    null,
                    null,
                    emptyMap(),
                    emptySet(),
                    additionalProperties = true,
                    isNullable = false
                ),
                false
            )
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    verify(
        """
           |@Serializable
           |data class Foo(
           |    val date: LocalDate? = null,
           |    val dateTime: LocalDateTime? = null,
           |    val uuid: Uuid? = null,
           |    val json: JsonElement? = null,
           |    val jsonArray: JsonArray? = null,
           |    val jsonObject: JsonObject? = null
           |)
           """.trimMargin(),
        primitiveImports,
        TypeName.Serializable,
        TypeName.Uuid,
        TypeName.Date,
        TypeName.DateTime,
        TypeName.JsonArray,
        TypeName.JsonElement,
        TypeName.JsonObject,
    )

    verify(
        """|@Serializable
           |data object EmptyObject
        """.trimMargin(),
        Model.Object(
            context = NamingContext.reference("EmptyObject", SchemaContext.Null),
            description = null,
            title = null,
            properties = emptyMap(),
            additionalProperties = Model.Object.AdditionalProperties.Allowed(false),
            isNullable = false
        ),
        TypeName.Serializable,
    )
}
