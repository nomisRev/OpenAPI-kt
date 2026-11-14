package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parsePathSegments
import io.github.nomisrev.openapi.toSteps
import io.github.nomisrev.openapi.pathTemplateProblems
import kotlin.test.assertEquals

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
        values = listOf(Model.EnumValue.String("queued"), Model.EnumValue.String("in-progress")),
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
        values = listOf(Model.EnumValue.String("queued")),
        default = null,
        description = null,
        title = null,
        isNullable = false,
    )
    val inProgressEnumType = Model.Enum(
        context = NamingContext.path(listOf("workflowId", "inProgress")),
        inner = stringType,
        values = listOf(Model.EnumValue.String("in-progress")),
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
        // The template is kept as written: a trailing slash and an empty segment are an empty literal segment.
        Input("/pets/", emptyMap(), listOf(PathSegment.Literal("pets"), PathSegment.Literal(""))),
        Input(
            "/pets/{petId}/",
            emptyMap(),
            listOf(PathSegment.Literal("pets"), PathSegment.Parameter("petId", stringType), PathSegment.Literal(""))
        ),
        Input("/a//b", emptyMap(), listOf(PathSegment.Literal("a"), PathSegment.Literal(""), PathSegment.Literal("b"))),
        // A request path is relative to the server URL and cannot start with `/`, so a leading empty segment is dropped.
        Input("//pets", emptyMap(), listOf(PathSegment.Literal("pets"))),
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
        ),
        // A segment of text and expressions is a composite; exactly `{name}`, or no braces, keeps its variant.
        Input(
            "/media/{id}.{ext}",
            mapOf("id" to intType),
            listOf(
                PathSegment.Literal("media"),
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Param("id", intType),
                        PathSegment.Composite.Part.Text("."),
                        PathSegment.Composite.Part.Param("ext", stringType),
                    )
                )
            )
        ),
        Input(
            "/hackathons/{id}.json",
            mapOf("id" to intType),
            listOf(
                PathSegment.Literal("hackathons"),
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Param("id", intType),
                        PathSegment.Composite.Part.Text(".json"),
                    )
                )
            )
        ),
        Input(
            "/lists.{format}",
            mapOf("format" to stringType),
            listOf(
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Text("lists."),
                        PathSegment.Composite.Part.Param("format", stringType),
                    )
                )
            )
        ),
        Input(
            "/v1/{name}:cancel",
            emptyMap(),
            listOf(
                PathSegment.Literal("v1"),
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Param("name", stringType),
                        PathSegment.Composite.Part.Text(":cancel"),
                    )
                )
            )
        ),
        Input(
            "/{a}{b}",
            mapOf("a" to intType, "b" to intType),
            listOf(
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Param("a", intType),
                        PathSegment.Composite.Part.Param("b", intType),
                    )
                )
            )
        ),
        Input(
            "/v{version}/x",
            mapOf("version" to intType),
            listOf(
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Text("v"),
                        PathSegment.Composite.Part.Param("version", intType),
                    )
                ),
                PathSegment.Literal("x")
            )
        ),
        // A fixed enum or a union inside a composite is a plain parameter.
        Input(
            "/w/{workflowId}.json",
            mapOf("workflowId" to flattenableUnionType),
            listOf(
                PathSegment.Literal("w"),
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Param("workflowId", flattenableUnionType),
                        PathSegment.Composite.Part.Text(".json"),
                    )
                )
            )
        ),
        // A fragment is kept on the last segment, as a part that is not sent.
        Input(
            "/tags/{arn}#tagKeys",
            mapOf("arn" to stringType),
            listOf(
                PathSegment.Literal("tags"),
                PathSegment.Composite(
                    listOf(
                        PathSegment.Composite.Part.Param("arn", stringType),
                        PathSegment.Composite.Part.Fragment("tagKeys"),
                    )
                )
            )
        ),
        // Not a plain expression name, an undeclared one and an unbalanced brace read as before.
        Input(
            "/a/{.format}",
            emptyMap(),
            listOf(PathSegment.Literal("a"), PathSegment.Parameter(".format", stringType))
        ),
        Input(
            "/a/{open",
            emptyMap(),
            listOf(PathSegment.Literal("a"), PathSegment.Literal("{open"))
        ),
        Input(
            "/a/{id}",
            emptyMap(),
            listOf(PathSegment.Literal("a"), PathSegment.Parameter("id", stringType))
        ),
    ).verifyAll("parsePathSegments") { input ->
        Eq(input.expected, parsePathSegments(input.path, input.pathParamTypes))
    }

    test("pathTemplateProblems reports what a path template does not allow, and nothing for a plain one") {
        assertEquals(emptyList(), pathTemplateProblems("/media/{id}.{ext}", setOf("id", "ext")))
        assertEquals(emptyList(), pathTemplateProblems("/pets/{petId}/", setOf("petId")))
        assertEquals(
            listOf(
                "Path '/tags/{arn}#tagKeys' has a fragment ('#tagKeys'), which a path template does not have. " +
                    "The fragment is not sent: the request is for 'tags/{arn}'. It stays in the generated names, " +
                    "so paths that differ only by their fragment stay separate."
            ),
            pathTemplateProblems("/tags/{arn}#tagKeys", setOf("arn"))
        )
        assertEquals(
            listOf(
                "Path '/a/{.format}' has the template expression '{.format}', which is not a plain parameter " +
                    "name. It is generated as a path parameter named '.format'."
            ),
            pathTemplateProblems("/a/{.format}", setOf(".format"))
        )
        assertEquals(
            listOf("Path '/a/{open' has an unbalanced brace in the segment '{open'. It is sent as written."),
            pathTemplateProblems("/a/{open", emptySet())
        )
        assertEquals(
            listOf(
                "Path '/a/{id}.json' has the template expression '{id}' without a declared path parameter. " +
                    "It is generated as a required string parameter."
            ),
            pathTemplateProblems("/a/{id}.json", emptySet())
        )
        assertEquals(
            listOf(
                "Path '/a' declares the path parameter 'id', which matches no template expression in the path. " +
                    "It is not sent."
            ),
            pathTemplateProblems("/a", setOf("id"))
        )
    }

    // A segment is navigated one step per part: a parameter, or the alphanumeric content of a text. Text without any
    // is only the way two steps are joined on the wire.
    fun steps(path: String, types: Map<String, Model> = emptyMap()): List<String> =
        parsePathSegments(path, types).flatMap { it.toSteps() }.map { step ->
            val name = when (val segment = step.segment) {
                is PathSegment.Parameter -> "{${segment.name}}"
                is PathSegment.Literal -> segment.name
                else -> error("unexpected $segment")
            }
            "${step.joinedBy}$name${step.trailing}"
        }

    test("toSteps splits a composite segment into one step per part") {
        assertEquals(listOf("/media", "/{id}", ".{extension}"), steps("/media/{id}.{extension}"))
        assertEquals(listOf("/hackathons", "/{id}", ".json"), steps("/hackathons/{id}.json"))
        assertEquals(listOf("/lists", ".{format}"), steps("/lists.{format}"))
        assertEquals(listOf("/v1", "/{name}", ":cancel"), steps("/v1/{name}:cancel"))
        assertEquals(listOf("/v", "{version}", "/x"), steps("/v{version}/x"))
        assertEquals(listOf("/ranges", "/{lo}", "-{hi}"), steps("/ranges/{lo}-{hi}"))
        assertEquals(listOf("/{a}", "{b}"), steps("/{a}{b}"))
        assertEquals(listOf("/tags", "/{arn}", "#tagKeys"), steps("/tags/{arn}#tagKeys"))
        // Text after the last step is kept for the identity of the node, not as a step.
        assertEquals(listOf("/a", "/{id}."), steps("/a/{id}."))
        // Plain segments are one step joined by `/`.
        assertEquals(listOf("/pets", "/{petId}", "/"), steps("/pets/{petId}/"))
    }
}
