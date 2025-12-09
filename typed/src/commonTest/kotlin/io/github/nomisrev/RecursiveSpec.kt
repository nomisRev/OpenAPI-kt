package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.ResolvedSchema

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
            ExpectedApi(
                recursiveAnchor,
                expected,
                api.reference("Root", recursiveAnchor),
                listOf(root)
            )
        }

    verifyAll("Recursive references", recursiveReferences) { schema -> ResolvedSchema.Reference(root, schema) }
}
