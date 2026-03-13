package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.KFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.render.attemptDeserialize
import io.github.nomisrev.openapi.routes.SchemaContext

val renderObjectSpec by testSuite {
    verifyKotlinFiles(
        name = "empty object renders as data object",
        resourceDirectory = "obj/empty",
    ) {
        listOf(
            Model.Object(
                NamingContext.reference("Foo", SchemaContext.Null),
                null,
                null,
                emptyMap(),
                false,
                false
            )
        ).generate("obj.empty")
    }

    verifyKotlinFiles(
        name = "value class with String",
        resourceDirectory = "obj/value-cls",
    ) {
        listOf(
            Model.Object.value(
                NamingContext.Reference("Foo", SchemaContext.Null),
                Model.Primitive.String(null, null, null, false, null)
            )
        ).generate("obj.value.cls")
    }

    verifyKotlinFiles(
        name = "value class with nullable String still renders non-null value",
        resourceDirectory = "obj/value-cls-nullable",
    ) {
        listOf(
            Model.Object.value(
                NamingContext.Reference("Foo", SchemaContext.Null),
                Model.Primitive.String(null, null, null, true, null)
            )
        ).generate("obj.value.cls.nullable")
    }

    verifyKotlinFiles(
        name = "single line data class with properties",
        resourceDirectory = "obj/single-line",
    ) {
        listOf(
            Model.Object(
                context = NamingContext.reference("Foo", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
                    "email" to Model.Object.Property(Model.Primitive.Long(null, null, null, true, null), false),
                    "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, false, null), true),
                    "longername" to Model.Object.Property(Model.Primitive.Double(null, null, null, true, null), true),
                ),
                additionalProperties = false,
                isNullable = false
            )
        ).generate("obj.single.line")
    }

    verifyKotlinFiles(
        name = "multi line data class with properties",
        resourceDirectory = "obj/multi-line",
    ) {
        listOf(
            Model.Object(
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
                additionalProperties = false,
                isNullable = false
            )
        ).generate("obj.multi.line")
    }

    verifyKotlinFiles(
        name = "value class with nested enum",
        resourceDirectory = "obj/nested-enum",
    ) {
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
        listOf(
            Model.Object.value(
                NamingContext.Reference("Foo", SchemaContext.Null),
                enum
            )
        ).generate("obj.nested.enum")
    }

    verifyKotlinFiles(
        name = "obj with primitive imports",
        resourceDirectory = "obj/primitive-imports",
    ) {
        listOf(
            Model.Object(
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
                            additionalProperties = true,
                            isNullable = false
                        ),
                        false
                    )
                ),
                additionalProperties = false,
                isNullable = false
            )
        ).generate("obj.primitive.imports")
    }

    verifyKotlinFiles(
        name = "empty object with explicit no additional properties",
        resourceDirectory = "obj/empty-no-additional",
    ) {
        listOf(
            Model.Object(
                context = NamingContext.reference("EmptyObject", SchemaContext.Null),
                description = null,
                title = null,
                properties = emptyMap(),
                additionalProperties = Model.Object.AdditionalProperties.Allowed(false),
                isNullable = false
            )
        ).generate("obj.empty.no.additional")
    }

    verifyKotlinFiles(
        name = "obj with additional properties as JsonObject",
        resourceDirectory = "obj/additional-properties-json",
    ) {
        listOf(
            Model.Object(
                NamingContext.reference("PersonWithAdditionalProperties", SchemaContext.Null),
                null,
                null,
                mapOf(
                    "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
                    "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, true, null), true),
                    "nested" to nested(),
                ),
                additionalProperties = true,
                isNullable = false
            )
        ).generate("obj.additional.properties.json") + KFile(
            "AttemptDeserialize.kt",
            "obj.additional.properties.json.model",
            attemptDeserialize("obj.additional.properties.json")
        )
    }

    verifyKotlinFiles(
        name = "obj with typed additional properties",
        resourceDirectory = "obj/additional-properties-typed",
    ) {
        listOf(
            Model.Object(
                NamingContext.reference("PersonWithAdditionalProperties", SchemaContext.Null),
                null,
                null,
                mapOf(
                    "name" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true),
                    "age" to Model.Object.Property(Model.Primitive.Int(null, null, null, true, null), true)
                ),
                additionalProperties = Model.Object.AdditionalProperties.Schema(nested().model),
                isNullable = false
            )
        ).generate("obj.additional.properties.typed")
    }

    verifyKotlinFiles(
        name = "required nullable has no implicit null default and required default uses annotation",
        resourceDirectory = "obj/required-nullable-default",
    ) {
        listOf(
            Model.Object(
                context = NamingContext.reference("Foo", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "requiredNullable" to Model.Object.Property(
                        Model.Primitive.String(default = null, description = null, constraint = null, isNullable = true, title = null),
                        true
                    ),
                    "requiredWithDefault" to Model.Object.Property(
                        Model.Primitive.String(
                            default = Model.Default.Value("default-value"),
                            description = null,
                            constraint = null,
                            isNullable = false,
                            title = null
                        ),
                        true
                    )
                ),
                additionalProperties = false,
                isNullable = false
            )
        ).generate("obj.required.nullable.default")
    }

    verifyKotlinFiles(
        name = "single-property object with JsonObject additional properties renders as data class",
        resourceDirectory = "obj/single-property-additional-json",
    ) {
        listOf(
            Model.Object(
                context = NamingContext.reference("SinglePropertyJsonAdditional", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "name" to Model.Object.Property(
                        Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false, title = null),
                        true
                    )
                ),
                additionalProperties = true,
                isNullable = false
            )
        ).generate("obj.single.property.additional.json")
    }

    verifyKotlinFiles(
        name = "single-property object with typed additional properties keeps empty map behavior",
        resourceDirectory = "obj/single-property-additional-typed",
    ) {
        listOf(
            Model.Object(
                context = NamingContext.reference("SinglePropertyTypedAdditional", SchemaContext.Null),
                description = null,
                title = null,
                properties = mapOf(
                    "name" to Model.Object.Property(
                        Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false, title = null),
                        true
                    )
                ),
                additionalProperties = Model.Object.AdditionalProperties.Schema(
                    Model.Primitive.Int(default = null, description = null, constraint = null, isNullable = false, title = null)
                ),
                isNullable = false
            )
        ).generate("obj.single.property.additional.typed")
    }
}

private fun nested(): Model.Object.Property = Model.Object.Property(
    Model.Object(
        NamingContext.reference("Foo", SchemaContext.Null)
            .nest(NamingContext.ObjectProperty("nestedClass")),
        null,
        null,
        mapOf(
            "config1" to Model.Object.Property(
                Model.Primitive.String(null, null, null, false, null),
                true
            ),
            "config2" to Model.Object.Property(
                Model.Primitive.Long(null, null, null, false, null),
                true
            ),
        ),
        false,
        false
    ), true
)
