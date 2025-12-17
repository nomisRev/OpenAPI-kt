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
            emptyList(),
            emptySet(),
            false,
            false
        )
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(val value: String)""".trimMargin(),
        Model.Object.value(
            NamingContext.Reference("Foo", SchemaContext.Null),
            Model.Primitive.String(null, null, null, false, null)
        )
    )

    verify(
        """|@Serializable
           |@JvmInline
           |value class Foo(val value: String)""".trimMargin(),
        Model.Object.value(
            NamingContext.Reference("Foo", SchemaContext.Null),
            Model.Primitive.String(null, null, null, true, null)
        )
    )

    val singleline = Model.Object(
        context = NamingContext.reference("Foo", SchemaContext.Null),
        description = null,
        title = null,
        properties = listOf(
            Model.Object.Property("name", Model.Primitive.String(null, null, null, false, null), false),
            Model.Object.Property("email", Model.Primitive.Long(null, null, null, true, null), false),
            Model.Object.Property("age", Model.Primitive.Int(null, null, null, false, null), true),
            Model.Object.Property("longername", Model.Primitive.Double(null, null, null, true, null), true),
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    verify(
        """|@Serializable
           |data class Foo(val name: String, val email: Long? = null, val age: Int, val longername: Double?)
           """.trimMargin(),
        singleline
    )

    val multiline = Model.Object(
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
           |    val age: Int,
           |    val longername: Double?,
           |    val longername2: Float,
           |    @SerialName("longer_name_3") val longerName3: Uuid,
           |    val longername4: LocalDateTime
           |)""".trimMargin(),
        multiline,
        setOf(TypeName.Uuid, TypeName.DateTime)
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
        nestedEnum
    )

    val primitiveImports = Model.Object(
        context = NamingContext.reference("Foo", SchemaContext.Null),
        description = null,
        title = null,
        properties = listOf(
            Model.Object.Property("date", Model.Date(null, false, null), false),
            Model.Object.Property("dateTime", Model.DateTime(null, false, null), false),
            Model.Object.Property("uuid", Model.Uuid(null, false, null), false),
            Model.Object.Property("json", Model.FreeFormJson(null, null, false, null), false),
            Model.Object.Property(
                "jsonArray", Model.Collection(
                    Model.FreeFormJson(null, null, false, null), null, null, null, false, null
                ), false
            ),
            Model.Object.Property(
                "jsonObject",
                Model.Object(
                    NamingContext.reference("Foo", SchemaContext.Null)
                        .nest(NamingContext.ObjectProperty("jsonObject")),
                    null,
                    null,
                    emptyList(),
                    emptySet(),
                    true,
                    false
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
           |    val date: LocalDate,
           |    val dateTime: LocalDateTime,
           |    val uuid: Uuid,
           |    val json: JsonElement,
           |    val jsonArray: JsonArray,
           |    val jsonObject: JsonObject
           |)
           """.trimMargin(),
        primitiveImports,
        setOf(
            TypeName.Uuid,
            TypeName.Date,
            TypeName.DateTime,
            TypeName.JsonArray,
            TypeName.JsonElement,
            TypeName.JsonObject,
        )
    )

    verify(
        """|@Serializable
           |data object EmptyObject
        """.trimMargin(),
        Model.Object(
            context = NamingContext.reference("EmptyObject", SchemaContext.Null),
            description = null,
            title = null,
            properties = emptyList(),
            inline = emptySet(),
            additionalProperties = Model.Object.AdditionalProperties.Allowed(false),
            isNullable = false
        )
    )
}
