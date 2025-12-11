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
                listOf(
                    Model.Object.Property(
                        "self",
                        Model.Reference(name, description.expected, isNullable ?: false),
                        isRequired = false
                    )
                ),
                inline = emptySet(),
                additionalProperties = false,
                isNullable = isNullable ?: false
            )
            recursiveAnchor expect expected
        }

    verifyAll("Recursive anchors", recursiveAnchors(NamingContext.ObjectProperty("test")))

    val root = NamingContext.Reference("Root", null)
    verifyAll(
        "Recursive anchors reference",
        recursiveAnchors(root).map { (schema, model) ->
            ExpectedApi(
                schema,
                model,
                api.reference("Root", schema),
                listOf(root)
            )
        }
    ) { schema -> ResolvedSchema.Reference(root, schema) }


    val recursiveReferences =
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
                listOf(
                    Model.Object.Property(
                        "self",
                        Model.Reference(root, description.expected, isNullable ?: false),
                        isRequired = false
                    )
                ),
                inline = emptySet(),
                additionalProperties = false,
                isNullable = isNullable ?: false
            )
            recursiveAnchor expect expected
        }

    verifyAll("Recursive references", recursiveReferences, root)

    val nestedRecursiveReferences =
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
                listOf(
                    Model.Object.Property(
                        "self",
                        Model.Collection(
                            Model.Reference(root, description.expected, isNullable ?: false),
                            null,
                            null,
                            null,
                            false
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

    verifyAll("Recursive nested collection references", nestedRecursiveReferences, root)

    val A = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("b" to ReferenceOr.schema("B"))
    )
    // {":{"type":"Object","context":{"type":"Reference","name":"B"},"properties":[{"baseName":"a","model":{"type":"io.github.nomisrev.openapi.Model.Reference","context":{"type":"Reference","name":"A"},"isNullable":false},"isRequired":false}],"inline":[],"additionalProperties":false,"isNullable":false},"isRequired":false}],"inline":["type":"Allowed"],"additionalProperties":false,"isNullable":false}
    val expectedA = Model.Object(
        NamingContext.Reference("A", null), null, listOf(
            Model.Object.Property(
                "b",
                Model.Object(
                    NamingContext.Reference("B", null), null,
                    listOf(
                        Model.Object.Property(
                            "a",
                            Model.Reference(NamingContext.Reference("A", null), null, false),
                            false
                        )
                    ), emptySet(), false, false
                ),
                false
            )
        ), emptySet(), false, false
    )

    val B = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("a" to ReferenceOr.schema("A"))
    )

    val expectedB = Model.Object(
        NamingContext.Reference("B", null), null, listOf(
            Model.Object.Property(
                "a",
                Model.Object(
                    NamingContext.Reference("A", null), null,
                    listOf(
                        Model.Object.Property(
                            "b",
                            Model.Reference(NamingContext.Reference("B", null), null, false),
                            false
                        )
                    ), emptySet(), false, false
                ),
                false
            )
        ), emptySet(), false, false
    )

    test("Indirect recursion") {
        val api = api.reference("A", A).reference("B", B)
        registry(api) {
            assertEq(
                expectedA,
                ReferenceOr.schema("A").toModel(NamingContext.Reference("A", null), SchemaContext.Write)
            )
            assertEq(
                expectedB,
                ReferenceOr.schema("B").toModel(NamingContext.Reference("B", null), SchemaContext.Write)
            )
        }
    }
}
