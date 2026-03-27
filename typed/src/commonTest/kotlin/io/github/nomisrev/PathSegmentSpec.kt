package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.UnionDispatch
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
    val workflowEnumType = Model.Enum(
        context = NamingContext.path("workflowId"),
        inner = stringType,
        values = listOf("queued", "in-progress"),
        default = null,
        description = null,
        title = null,
        isNullable = false,
    )
    val flattenableUnionType = Model.OneOf(
        context = NamingContext.path("workflowId"),
        cases = listOf(
            Model.Union.Case(intType, emptySet()),
            Model.Union.Case(workflowEnumType, emptySet()),
        ),
        default = null,
        description = null,
        title = null,
        dispatch = UnionDispatch.Structural,
        isNullable = false,
    )
    val queuedEnumType = Model.Enum(
        context = NamingContext.path(listOf("workflowId", "queued")),
        inner = stringType,
        values = listOf("queued"),
        default = null,
        description = null,
        title = null,
        isNullable = false,
    )
    val inProgressEnumType = Model.Enum(
        context = NamingContext.path(listOf("workflowId", "inProgress")),
        inner = stringType,
        values = listOf("in-progress"),
        default = null,
        description = null,
        title = null,
        isNullable = false,
    )
    val multiEnumUnionType = Model.OneOf(
        context = NamingContext.path("workflowId"),
        cases = listOf(
            Model.Union.Case(queuedEnumType, emptySet()),
            Model.Union.Case(inProgressEnumType, emptySet()),
        ),
        default = null,
        description = null,
        title = null,
        dispatch = UnionDispatch.Structural,
        isNullable = false,
    )
    val objectType = Model.Object(
        context = NamingContext.path("payload"),
        description = null,
        title = null,
        properties = emptyMap(),
        additionalProperties = false,
        isNullable = false,
    )
    val nonFlattenableUnionType = Model.OneOf(
        context = NamingContext.path("payload"),
        cases = listOf(
            Model.Union.Case(stringType, emptySet()),
            Model.Union.Case(objectType, emptySet()),
        ),
        default = null,
        description = null,
        title = null,
        dispatch = UnionDispatch.Structural,
        isNullable = false,
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
        ),
        Input(
            "/workflows/{workflowId}",
            mapOf("workflowId" to flattenableUnionType),
            listOf(
                PathSegment.Literal("workflows"),
                PathSegment.OverloadedParameter("workflowId", flattenableUnionType)
            )
        ),
        Input(
            "/workflows/{workflowId}",
            mapOf("workflowId" to multiEnumUnionType),
            listOf(
                PathSegment.Literal("workflows"),
                PathSegment.OverloadedParameter("workflowId", multiEnumUnionType)
            )
        ),
        Input(
            "/payloads/{payload}",
            mapOf("payload" to nonFlattenableUnionType),
            listOf(
                PathSegment.Literal("payloads"),
                PathSegment.Parameter("payload", nonFlattenableUnionType)
            )
        )
    ).verifyAll("parsePathSegments") { input ->
        Eq(input.expected, parsePathSegments(input.path, input.pathParamTypes))
    }
}
