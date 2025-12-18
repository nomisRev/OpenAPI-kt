package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import kotlin.collections.emptySet

val recursiveSpec by testSuite {
    fun recursiveAnchors(name: NamingContext) =
        listOf(true, false, null).product(description) { isNullable, description ->
            val recursiveAnchor = Schema(
                recursiveAnchor = true,
                type = Schema.Type.Basic.Object,
                properties = mapOf("self" to ReferenceOr.Reference("#")),
                description = description.actual,
                nullable = isNullable
            )
            val expected = Model.Object(
                name,
                description.expected,
                null,
                mapOf(
                    "self" to Model.Object.Property(
                        Model.Reference(name, description.expected, isNullable ?: false, null),
                        isRequired = false
                    )
                ),
                inline = emptySet(),
                additionalProperties = false,
                isNullable = isNullable ?: false
            )
            recursiveAnchor expect expected
        }

    verifyAll("Recursive anchors", recursiveAnchors(NamingContext.path("test")))

    val rootRef = NamingContext.Reference("Root", SchemaContext.Null)
    val root = NamingContext.reference("Root", SchemaContext.Null)
    verifyAll(
        "Recursive anchors reference",
        recursiveAnchors(root).map { (schema, model) ->
            ExpectedApi(
                schema,
                model,
                api.reference("Root", schema),
                listOf(rootRef)
            )
        }
    ) { schema -> ResolvedSchema.Reference(rootRef, schema) }

    val recursiveReferences: List<Expect<Schema, Model.Object>> =
        listOf(true, false, null).product(description) { isNullable, description ->
            val recursiveAnchor = Schema(
                recursiveAnchor = true,
                type = Schema.Type.Basic.Object,
                properties = mapOf("self" to ReferenceOr.schema("Root")),
                description = description.actual,
                nullable = isNullable
            )
            val expected = Model.Object(
                root,
                description.expected,
                null,
                mapOf(
                    "self" to Model.Object.Property(
                        Model.Reference(root, description.expected, isNullable ?: false, null),
                        isRequired = false
                    )
                ),
                inline = emptySet(),
                additionalProperties = false,
                isNullable = isNullable ?: false
            )
            recursiveAnchor expect expected
        }

    verifyAll("Recursive references", recursiveReferences, NamingContext.Reference("Root", SchemaContext.Null))

    val nestedRecursiveReferences: List<Expect<Schema, Model.Object>> =
        listOf(true, false, null).product(description) { isNullable, description ->
            val recursiveAnchor = Schema(
                recursiveAnchor = true,
                type = Schema.Type.Basic.Object,
                properties = mapOf(
                    "self" to ReferenceOr.value(
                        Schema(
                            type = Schema.Type.Basic.Array,
                            items = ReferenceOr.schema("Root")
                        )
                    )
                ),
                description = description.actual,
                nullable = isNullable
            )
            val expected = Model.Object(
                root,
                description.expected,
                null,
                listOf(
                    Model.Object.Property(
                        "self",
                        Model.Collection(
                            Model.Reference(root, description.expected, isNullable ?: false, null),
                            null,
                            null,
                            null,
                            false,
                            null
                        ),
                        isRequired = false
                    )
                ),
                inline = emptySet(),
                additionalProperties = false,
                isNullable = isNullable ?: false
            )
            recursiveAnchor expect expected
        }

    verifyAll(
        "Recursive nested collection references",
        nestedRecursiveReferences,
        NamingContext.Reference("Root", SchemaContext.Null)
    )

    val A = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("b" to ReferenceOr.schema("B"))
    )
    val expectedA = Model.Object(
        context = NamingContext.reference("A", SchemaContext.Null),
        description = null,
        title = null,
        properties = listOf(
            Model.Object.Property(
                baseName = "b",
                model = Model.Object(
                    NamingContext.reference("B", SchemaContext.Null),
                    description = null,
                    title = null,
                    properties = listOf(
                        Model.Object.Property(
                            baseName = "a",
                            model = Model.Reference(
                                NamingContext.reference("A", SchemaContext.Null),
                                null,
                                false,
                                null
                            ),
                            isRequired = false
                        )
                    ),
                    inline = emptySet(),
                    additionalProperties = false,
                    isNullable = false
                ),
                isRequired = false
            )
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    val B = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("a" to ReferenceOr.schema("A"))
    )

    val expectedB = Model.Object(
        context = NamingContext.reference("B", SchemaContext.Null),
        description = null,
        title = null,
        properties = listOf(
            Model.Object.Property(
                baseName = "a",
                model = Model.Object(
                    context = NamingContext.reference("A", SchemaContext.Null),
                    description = null,
                    title = null,
                    properties = listOf(
                        Model.Object.Property(
                            "b",
                            Model.Reference(NamingContext.reference("B", SchemaContext.Null), null, false, null),
                            false
                        )
                    ),
                    inline = emptySet(),
                    additionalProperties = false,
                    isNullable = false
                ),
                isRequired = false
            )
        ),
        inline = emptySet(),
        additionalProperties = false,
        isNullable = false
    )

    test("Indirect recursion") {
        val api = api.reference("A", A).reference("B", B)
        registry(api) {
            assertEq(
                expectedA,
                ReferenceOr.schema("A").toModel(NamingContext.Reference("A", SchemaContext.Null), SchemaContext.Write)
            )
            assertEq(
                expectedB,
                ReferenceOr.schema("B").toModel(NamingContext.Reference("B", SchemaContext.Null), SchemaContext.Write)
            )
        }
    }
}
