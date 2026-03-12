package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.api
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.generate
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.reference

/**
 * Tests for union (oneOf/anyOf) code generation including edge cases.
 *
 * Covers various scenarios that can occur with oneOf/anyOf in OpenAPI specs:
 * - Basic unions with primitives, objects, references, collections
 * - Discriminated unions with type discriminators
 * - Edge cases: single case unions, overlapping properties, nested collections
 * - Known limitations: nested unions, discriminated objects in unions, recursive unions
 */
val unionRenderSpec by testSuite {
    val union = NamingContext.reference("Union", SchemaContext.Null)

    fun objectCase(
        name: String,
        vararg props: Pair<String, Model>,
        additionalProperties: Boolean = false,
    ) = Model.Object(
        context = NamingContext(
            NamingContext.Reference("Union", SchemaContext.Null),
            listOf(NamingContext.UnionCase(name))
        ),
        description = null,
        title = null,
        properties = props.associate { (k, v) -> k to Model.Object.Property(v, true) },
        additionalProperties = additionalProperties,
        isNullable = false
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

    verifyKotlinFiles(
        name = "union renders all primitive cases",
        resourceDirectory = "union/all-primitives"
    ) {
        listOf(
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
            )
        ).generate("union.all.primitives")
    }

    // ==================== UNION CASE NAMING ====================

    // Union case names are derived from property names joined with "And"
    // Falls back to CaseIndex if the generated name exceeds 90 characters
    verifyKotlinFiles(
        name = "union case naming uses property-derived names",
        resourceDirectory = "union/case-naming"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                    Model.Union.Case(employeeCase(NamingContext.UnionCase("AgeAndName")), null),
                ),
                null,
                null,
                null,
                null,
                false
            )
        ).generate("union.case.naming")
    }

    // ==================== DISCRIMINATED UNIONS ====================

    verifyKotlinFiles(
        name = "discriminated union with primitive and object cases",
        resourceDirectory = "union/discriminated-primitive"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(Model.Primitive.String(null, null, null, false, null), "reference"),
                    Model.Union.Case(employeeCase(NamingContext.UnionCase("employee")), "employee"),
                ),
                null,
                null,
                null,
                "\$type",
                false
            )
        ).generate("discriminated.primitive")
    }

    verifyKotlinFiles(
        name = "discriminated union with reference and object cases",
        resourceDirectory = "union/discriminated-reference"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(
                        Model.Reference(
                            NamingContext.reference("Person", SchemaContext.Null),
                            null,
                            false,
                            null
                        ),
                        "person"
                    ),
                    Model.Union.Case(employeeCase(NamingContext.UnionCase("employee")), "employee"),
                ),
                null,
                null,
                null,
                "\$type",
                false
            )
        ).generate("discriminated.reference")
    }

    // ==================== UNIONS WITH ENUMS ====================

    val aOrB = Model.Enum(
        context = union.nest(NamingContext.UnionCase("AscOrDesc")),
        inner = Model.Primitive.String(null, null, null, false, null),
        values = listOf("asc", "desc"),
        default = null,
        description = null,
        title = null,
        isNullable = false
    )

    verifyKotlinFiles(
        name = "union with enum and primitive cases",
        resourceDirectory = "union/enum-and-primitive"
    ) {
        listOf(
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
            )
        ).generate("union.enum.primitive")
    }

    // ==================== UNIONS WITH COLLECTIONS ====================

    verifyKotlinFiles(
        name = "union with list and primitive cases",
        resourceDirectory = "union/collection-and-primitive"
    ) {
        listOf(
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
            )
        ).generate("union.collection.primitive")
    }

    verifyKotlinFiles(
        name = "union with nested list and primitive cases",
        resourceDirectory = "union/nested-collection-and-primitive"
    ) {
        listOf(
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
            )
        ).generate("union.nested.collection.primitive")
    }

    // ==================== UNIONS WITH REFERENCES ====================

    verifyKotlinFiles(
        name = "union with reference cases",
        resourceDirectory = "union/references"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(
                        Model.Reference(
                            NamingContext.reference("Person", SchemaContext.Null),
                            null,
                            false,
                            null
                        ),
                        null
                    ),
                    Model.Union.Case(
                        Model.Reference(
                            NamingContext.reference("Company", SchemaContext.Null),
                            null,
                            false,
                            null
                        ),
                        null
                    ),
                ),
                null,
                null,
                null,
                null,
                false
            )
        ).generate("union.references")
    }

    // ==================== EDGE CASES ====================

    verifyKotlinFiles(
        name = "single case union still renders sealed interface",
        resourceDirectory = "union/single-case"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                ),
                null,
                null,
                null,
                null,
                false
            )
        ).generate("union.single.case")
    }

    verifyKotlinFiles(
        name = "overlapping object cases keep specific case first in deserialization",
        resourceDirectory = "union/overlapping-objects"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(
                        objectCase(
                            "AAndB",
                            "a" to Model.Primitive.String(null, null, null, false, null),
                            "b" to Model.Primitive.String(null, null, null, false, null)
                        ),
                        null
                    ),
                    Model.Union.Case(
                        objectCase(
                            "AAndBAndC",
                            "a" to Model.Primitive.String(null, null, null, false, null),
                            "b" to Model.Primitive.String(null, null, null, false, null),
                            "c" to Model.Primitive.String(null, null, null, false, null)
                        ),
                        null
                    ),
                ),
                null,
                null,
                null,
                null,
                false
            )
        ).generate("union.overlapping.objects")
    }

    verifyKotlinFiles(
        name = "nullable oneOf property is flattened to a nullable primitive",
        resourceDirectory = "union/nullable-oneof-property"
    ) {
        val schema = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "value" to ReferenceOr.value(
                    Schema(
                        oneOf = listOf(
                            ReferenceOr.value(Schema.string),
                            ReferenceOr.value(Schema.NULL)
                        )
                    )
                )
            ),
            required = listOf("value")
        )
        val model = registry(api.reference("Container", schema)) {
            ReferenceOr.schema("Container")
                .toModel(NamingContext.Reference("Container", SchemaContext.Null), SchemaContext.Write)
        }
        listOf(model).generate("union.nullable.oneof.property")
    }

    verifyKotlinFiles(
        name = "union with empty object case renders data object",
        resourceDirectory = "union/empty-obj-case"
    ) {
        listOf(
            Model.Union(
                context = union,
                cases = listOf(
                    Model.Union.Case(objectCase("Empty"), null),
                    Model.Union.Case(
                        objectCase("WithProp", "prop" to Model.Primitive.String(null, null, null, false, null)),
                        null
                    ),
                ),
                default = null,
                description = null,
                title = null,
                discriminator = null,
                isNullable = false
            )
        ).generate("union.empty.obj.case")
    }

    verifyKotlinFiles(
        name = "union additionalProperties object case is ordered last",
        resourceDirectory = "union/additional-properties-last"
    ) {
        listOf(
            Model.Union(
                context = union,
                cases = listOf(
                    Model.Union.Case(
                        objectCase("Strict", "name" to Model.Primitive.String(null, null, null, false, null)),
                        null
                    ),
                    Model.Union.Case(
                        objectCase(
                            "WithAdditional",
                            "name" to Model.Primitive.String(null, null, null, false, null),
                            additionalProperties = true,
                        ),
                        null
                    ),
                ),
                default = null,
                description = null,
                title = null,
                discriminator = null,
                isNullable = false
            )
        ).generate("union.additional.properties.last")
    }

    verifyKotlinFiles(
        name = "union free form json case is last in deserialization attempts",
        resourceDirectory = "union/free-form-json-last"
    ) {
        listOf(
            Model.Union(
                context = union,
                cases = listOf(
                    Model.Union.Case(
                        objectCase("Id", "id" to Model.Primitive.Int(null, null, null, false, null)),
                        null
                    ),
                    Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                    Model.Union.Case(Model.FreeFormJson(null, null, false, null), null),
                ),
                default = null,
                description = null,
                title = null,
                discriminator = null,
                isNullable = false
            )
        ).generate("union.free.form.json.last")
    }

    verifyKotlinFiles(
        name = "union with collection and single reference case",
        resourceDirectory = "union/collection-of-references"
    ) {
        listOf(
            Model.Union(
                context = union,
                listOf(
                    Model.Union.Case(
                        Model.Collection(
                            Model.Reference(NamingContext.reference("Item", SchemaContext.Null), null, false, null),
                            null,
                            null,
                            null,
                            false,
                            null
                        ),
                        null
                    ),
                    Model.Union.Case(
                        Model.Reference(NamingContext.reference("Item", SchemaContext.Null), null, false, null),
                        null
                    ),
                ),
                null,
                null,
                null,
                null,
                false
            )
        ).generate("union.collection.of.references")
    }

    verifyKotlinFiles(
        name = "nested union case renders wrapped value class and nested sealed union",
        resourceDirectory = "union/nested-union"
    ) {
        val nested = Model.Union(
            context = union.nest(NamingContext.UnionCase("Inner")),
            cases = listOf(
                Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), null),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false
        )

        listOf(
            Model.Union(
                context = union,
                cases = listOf(
                    Model.Union.Case(nested, null),
                    Model.Union.Case(Model.Primitive.Boolean(null, null, false, null), null),
                ),
                default = null,
                description = null,
                title = null,
                discriminator = null,
                isNullable = false
            )
        ).generate("union.nested.union")
    }

    verifyKotlinFiles(
        name = "nested discriminated object case renders wrapped value class",
        resourceDirectory = "union/nested-discriminated-object"
    ) {
        val authContext = union.nest(NamingContext.UnionCase("Auth"))
        val kindProp = Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
        val auth = Model.DiscriminatedObject(
            context = authContext,
            abstractProperties = mapOf("kind" to kindProp),
            subtypes = listOf(
                Model.Object(
                    context = authContext.nest(NamingContext.DiscriminatedObjectCase("password")),
                    description = null,
                    title = null,
                    properties = mapOf(
                        "kind" to kindProp,
                        "password" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
                    ),
                    additionalProperties = false,
                    isNullable = false
                ),
                Model.Object(
                    context = authContext.nest(NamingContext.DiscriminatedObjectCase("token")),
                    description = null,
                    title = null,
                    properties = mapOf(
                        "kind" to kindProp,
                        "token" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), true)
                    ),
                    additionalProperties = false,
                    isNullable = false
                )
            ),
            description = null,
            title = null,
            discriminator = "kind",
            isNullable = false
        )

        listOf(
            Model.Union(
                context = union,
                cases = listOf(
                    Model.Union.Case(auth, null),
                    Model.Union.Case(Model.Primitive.Int(null, null, null, false, null), null),
                ),
                default = null,
                description = null,
                title = null,
                discriminator = null,
                isNullable = false
            )
        ).generate("union.nested.discriminated.object")
    }

    verifyKotlinFiles(
        name = "recursive union through ref renders without infinite expansion",
        resourceDirectory = "union/recursive-ref"
    ) {
        val treeSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf("leaf" to ReferenceOr.value(Schema.string))
                    )
                ),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "children" to ReferenceOr.value(
                                Schema.list(ReferenceOr.schema("Tree"))
                            )
                        )
                    )
                )
            )
        )

        val model = registry(api.reference("Tree", treeSchema)) {
            ReferenceOr.schema("Tree")
                .toModel(NamingContext.Reference("Tree", SchemaContext.Null), SchemaContext.Write)
        }
        listOf(model).generate("union.recursive.ref")
    }
}
