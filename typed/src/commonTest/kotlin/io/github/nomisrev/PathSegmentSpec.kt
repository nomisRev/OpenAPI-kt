package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parsePathSegments

val PathSegmentSpec by testSuite {
    val stringType = Model.Primitive.String(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null
    )
    val intType = Model.Primitive.Int(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null
    )

    data class Input(
        val path: String,
        val pathParamTypes: Map<String, Model>,
        val expected: List<PathSegment>,
    )

    listOf(
        Input("/", emptyMap(), emptyList()),
        Input("/pets", emptyMap(), listOf(PathSegment.Literal("pets"))),
        Input(
            "/pets/{petId}",
            emptyMap(),
            listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", stringType)
            )
        ),
        Input(
            "/repos/{owner}/{repo}/collaborators",
            emptyMap(),
            listOf(
                PathSegment.Literal("repos"),
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType),
                PathSegment.Literal("collaborators")
            )
        ),
        Input(
            "/chat/completions",
            emptyMap(),
            listOf(PathSegment.Literal("chat"), PathSegment.Literal("completions"))
        ),
        Input(
            "/{owner}/{repo}",
            emptyMap(),
            listOf(
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType)
            )
        ),
        Input(
            "/pets/{petId}",
            mapOf("petId" to intType),
            listOf(PathSegment.Literal("pets"), PathSegment.Parameter("petId", intType))
        ),
        Input(
            "/pets/{petId}",
            mapOf("other" to intType),
            listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", stringType)
            )
        )
    ).verifyAll("parsePathSegments") { input ->
        Eq(input.expected, parsePathSegments(input.path, input.pathParamTypes))
    }
}
