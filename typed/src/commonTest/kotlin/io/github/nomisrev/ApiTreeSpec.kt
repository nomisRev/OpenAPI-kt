package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathNode
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.buildTree
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.toApiTree
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

val apiTreeSpec by testSuite {
    val stringType = Model.Primitive.String(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null,
    )
    val intType = Model.Primitive.Int(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null,
    )

    fun literal(name: String): PathSegment = PathSegment.Literal(name)
    fun param(name: String): PathSegment = PathSegment.Parameter(name, stringType)
    fun intParam(name: String): PathSegment = PathSegment.Parameter(name, intType)
    fun overloadedWorkflowParam(name: String, routeKey: String): PathSegment.OverloadedParameter {
        val enumType = Model.Enum(
            context = NamingContext.path(listOf("workflows", routeKey, "workflowId")),
            inner = stringType,
            values = listOf("queued", "in-progress"),
            default = null,
            description = null,
            title = null,
            isNullable = false,
        )
        val unionType = Model.Union(
            context = NamingContext.path(listOf("workflows", routeKey)),
            cases = listOf(
                Model.Union.Case(intType, discriminator = null),
                Model.Union.Case(enumType, discriminator = null),
            ),
            default = null,
            description = null,
            title = null,
            discriminator = null,
            isNullable = false,
        )
        return PathSegment.OverloadedParameter(name, unionType)
    }

    fun route(method: HttpMethod, vararg segments: PathSegment): Route = Route(
        summary = null,
        segments = segments.toList(),
        method = method,
        body = null,
        parameters = emptyList(),
        returns = Route.Returns(default = null, responses = emptyMap(), extensions = emptyMap()),
        extensions = emptyMap(),
        deprecated = false,
    )

    fun node(
        segment: PathSegment,
        operations: Map<HttpMethod, Route> = emptyMap(),
        children: List<PathNode> = emptyList(),
    ): PathNode = PathNode(segment, operations, children)

    fun overloadedCaseSignatures(segment: PathSegment.OverloadedParameter): List<String> =
        segment.cases.map { case ->
            when (val model = case.model) {
                is Model.Primitive.Int -> "Int"
                is Model.Enum -> "Enum(values=${model.values.joinToString(prefix = "[", postfix = "]")})"
                else -> error("Unexpected overloaded path segment model: $model")
            }
        }

    fun sharedWorkflowNode(tree: ApiTree): PathNode =
        tree.children.single().children.single()

    fun assertSharedPathParameterConflict(
        routes: List<Route>,
        expectedNode: String,
        vararg expectedFragments: String,
    ) {
        val messages = listOf(routes, routes.reversed()).map { ordered ->
            val error = assertFailsWith<IllegalArgumentException> {
                ordered.buildTree("Conflicts")
            }
            val message = requireNotNull(error.message)
            assertTrue(
                message.startsWith("Conflicting shared path parameter node '$expectedNode': "),
                message
            )
            expectedFragments.forEach { fragment ->
                assertTrue(message.contains(fragment), message)
            }
            message
        }
        assertEquals(messages.first(), messages.last())
    }

    test("buildTree empty routes") {
        assertEquals(
            ApiTree(name = "Empty", operations = emptyMap(), children = emptyList(), servers = emptyList()),
            emptyList<Route>().buildTree("Empty"),
        )
    }

    test("buildTree root-level operation") {
        val getRoot = route(HttpMethod.Get)
        assertEquals(
            ApiTree(name = "Root", operations = mapOf(HttpMethod.Get to getRoot), children = emptyList(), servers = emptyList()),
            listOf(getRoot).buildTree("Root"),
        )
    }

    test("buildTree single static path") {
        val getPets = route(HttpMethod.Get, literal("pets"))
        assertEquals(
            ApiTree(
                name = "Pets",
                operations = emptyMap(),
                children = listOf(node(literal("pets"), operations = mapOf(HttpMethod.Get to getPets))),
                servers = emptyList(),
            ),
            listOf(getPets).buildTree("Pets"),
        )
    }

    test("buildTree path with parameter") {
        val getPet = route(HttpMethod.Get, literal("pets"), param("petId"))
        assertEquals(
            ApiTree(
                name = "Pets",
                operations = emptyMap(),
                children = listOf(
                    node(
                        literal("pets"),
                        children = listOf(
                            node(param("petId"), operations = mapOf(HttpMethod.Get to getPet)),
                        ),
                    ),
                ),
                servers = emptyList(),
            ),
            listOf(getPet).buildTree("Pets"),
        )
    }

    test("buildTree shared prefix keeps parent operation and child path") {
        val getPets = route(HttpMethod.Get, literal("pets"))
        val getPet = route(HttpMethod.Get, literal("pets"), param("petId"))
        assertEquals(
            ApiTree(
                name = "Pets",
                operations = emptyMap(),
                children = listOf(
                    node(
                        literal("pets"),
                        operations = mapOf(HttpMethod.Get to getPets),
                        children = listOf(node(param("petId"), operations = mapOf(HttpMethod.Get to getPet))),
                    ),
                ),
                servers = emptyList(),
            ),
            listOf(getPets, getPet).buildTree("Pets"),
        )
    }

    test("buildTree merges multiple methods on same node") {
        val getPet = route(HttpMethod.Get, literal("pets"), param("petId"))
        val deletePet = route(HttpMethod.Delete, literal("pets"), param("petId"))
        assertEquals(
            ApiTree(
                name = "Pets",
                operations = emptyMap(),
                children = listOf(
                    node(
                        literal("pets"),
                        children = listOf(
                            node(
                                param("petId"),
                                operations = mapOf(
                                    HttpMethod.Get to getPet,
                                    HttpMethod.Delete to deletePet,
                                ),
                            ),
                        ),
                    ),
                ),
                servers = emptyList(),
            ),
            listOf(getPet, deletePet).buildTree("Pets"),
        )
    }

    test("buildTree deep nesting") {
        val getCollaborators = route(
            HttpMethod.Get,
            literal("repos"),
            param("owner"),
            param("repo"),
            literal("collaborators"),
        )
        assertEquals(
            ApiTree(
                name = "GitHub",
                operations = emptyMap(),
                children = listOf(
                    node(
                        literal("repos"),
                        children = listOf(
                            node(
                                param("owner"),
                                children = listOf(
                                    node(
                                        param("repo"),
                                        children = listOf(
                                            node(
                                                literal("collaborators"),
                                                operations = mapOf(HttpMethod.Get to getCollaborators),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
                servers = emptyList(),
            ),
            listOf(getCollaborators).buildTree("GitHub"),
        )
    }

    test("buildTree consecutive parameters") {
        val getParams = route(HttpMethod.Get, param("a"), param("b"))
        assertEquals(
            ApiTree(
                name = "Params",
                operations = emptyMap(),
                children = listOf(
                    node(
                        param("a"),
                        children = listOf(node(param("b"), operations = mapOf(HttpMethod.Get to getParams))),
                    ),
                ),
                servers = emptyList(),
            ),
            listOf(getParams).buildTree("Params"),
        )
    }

    test("buildTree multiple branches") {
        val getPets = route(HttpMethod.Get, literal("pets"))
        val getUsers = route(HttpMethod.Get, literal("users"))
        assertEquals(
            ApiTree(
                name = "Branches",
                operations = emptyMap(),
                children = listOf(
                    node(literal("pets"), operations = mapOf(HttpMethod.Get to getPets)),
                    node(literal("users"), operations = mapOf(HttpMethod.Get to getUsers)),
                ),
                servers = emptyList(),
            ),
            listOf(getPets, getUsers).buildTree("Branches"),
        )
    }

    test("buildTree reuses shared overloaded parameter node for compatible sibling routes in any order") {
        val getRuns = route(
            HttpMethod.Get,
            literal("workflows"),
            overloadedWorkflowParam("workflowId", "runs"),
            literal("runs"),
        )
        val getHistory = route(
            HttpMethod.Get,
            literal("workflows"),
            overloadedWorkflowParam("workflowId", "history"),
            literal("history"),
        )

        val forwardNode = sharedWorkflowNode(listOf(getRuns, getHistory).buildTree("Workflows"))
        val reverseNode = sharedWorkflowNode(listOf(getHistory, getRuns).buildTree("Workflows"))
        val forwardSegment = assertIs<PathSegment.OverloadedParameter>(forwardNode.segment)
        val reverseSegment = assertIs<PathSegment.OverloadedParameter>(reverseNode.segment)

        assertEquals("workflowId", forwardSegment.name)
        assertEquals(listOf("Int", "Enum(values=[queued, in-progress])"), overloadedCaseSignatures(forwardSegment))
        assertEquals(overloadedCaseSignatures(forwardSegment), overloadedCaseSignatures(reverseSegment))
        assertEquals(setOf("runs", "history"), forwardNode.children.map { it.segment.name }.toSet())
        assertEquals(setOf("runs", "history"), reverseNode.children.map { it.segment.name }.toSet())
    }

    test("buildTree rejects mixed parameter and overloaded parameter on shared node regardless of insertion order") {
        val getRuns = route(
            HttpMethod.Get,
            literal("workflows"),
            overloadedWorkflowParam("workflowId", "runs"),
            literal("runs"),
        )
        val getHistory = route(
            HttpMethod.Get,
            literal("workflows"),
            param("workflowId"),
            literal("history"),
        )

        assertSharedPathParameterConflict(
            routes = listOf(getRuns, getHistory),
            expectedNode = "/workflows/{workflowId}",
            "GET /workflows/{workflowId}/history",
            "GET /workflows/{workflowId}/runs",
            "Parameter(name=workflowId, model=String)",
            "OverloadedParameter(name=workflowId, type=Union(cases=[Int, Enum(values=[queued, in-progress])]))",
        )
    }

    test("buildTree rejects different plain parameter models on shared node regardless of insertion order") {
        val getPet = route(HttpMethod.Get, literal("pets"), param("petId"))
        val deletePet = route(HttpMethod.Delete, literal("pets"), intParam("petId"))

        assertSharedPathParameterConflict(
            routes = listOf(getPet, deletePet),
            expectedNode = "/pets/{petId}",
            "DELETE /pets/{petId}",
            "GET /pets/{petId}",
            "Parameter(name=petId, model=Int)",
            "Parameter(name=petId, model=String)",
        )
    }

    test("buildTree realistic mixed API fragment") {
        val getRoot = route(HttpMethod.Get)
        val getRepo = route(HttpMethod.Get, literal("repos"), param("owner"), param("repo"))
        val deleteRepo = route(HttpMethod.Delete, literal("repos"), param("owner"), param("repo"))
        val getCollaborators = route(
            HttpMethod.Get,
            literal("repos"),
            param("owner"),
            param("repo"),
            literal("collaborators"),
        )
        val postIssues = route(HttpMethod.Post, literal("repos"), param("owner"), param("repo"), literal("issues"))
        val getUser = route(HttpMethod.Get, literal("users"), param("username"))

        assertEquals(
            ApiTree(
                name = "GitHub",
                operations = mapOf(HttpMethod.Get to getRoot),
                children = listOf(
                    node(
                        literal("repos"),
                        children = listOf(
                            node(
                                param("owner"),
                                children = listOf(
                                    node(
                                        param("repo"),
                                        operations = mapOf(
                                            HttpMethod.Get to getRepo,
                                            HttpMethod.Delete to deleteRepo,
                                        ),
                                        children = listOf(
                                            node(
                                                literal("collaborators"),
                                                operations = mapOf(HttpMethod.Get to getCollaborators),
                                            ),
                                            node(
                                                literal("issues"),
                                                operations = mapOf(HttpMethod.Post to postIssues),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                    node(
                        literal("users"),
                        children = listOf(
                            node(param("username"), operations = mapOf(HttpMethod.Get to getUser)),
                        ),
                    ),
                ),
                servers = emptyList(),
            ),
            listOf(getRoot, getRepo, deleteRepo, getCollaborators, postIssues, getUser).buildTree("GitHub"),
        )
    }

    test("Route.path computed property reconstructs template") {
        data class Case(val route: Route, val expected: String)

        val cases = listOf(
            Case(route(HttpMethod.Get), "/"),
            Case(route(HttpMethod.Get, literal("pets")), "/pets"),
            Case(route(HttpMethod.Get, literal("pets"), param("petId")), "/pets/{petId}"),
            Case(route(HttpMethod.Get, param("a"), param("b")), "/{a}/{b}"),
            Case(
                route(
                    HttpMethod.Get,
                    literal("repos"),
                    param("owner"),
                    param("repo"),
                    literal("collaborators"),
                ),
                "/repos/{owner}/{repo}/collaborators",
            ),
        )

        for (case in cases) {
            assertEquals(case.expected, case.route.path)
        }
    }

    test("toApiTree keeps referenced models reachable from overloaded request body cases") {
        val api = OpenAPI(
            info = Info("Test", "1.0"),
            paths = mapOf(
                "/pets" to PathItem(
                    post = Operation(
                        requestBody = ReferenceOr.value(
                            RequestBody(
                                required = true,
                                content = mapOf(
                                    ContentType.Application.Json.toString() to MediaType(
                                        schema = ReferenceOr.value(
                                            Schema(
                                                oneOf = listOf(
                                                    ReferenceOr.schema("CreatePet"),
                                                    ReferenceOr.value(Schema.string),
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        responses = Responses(200, Response())
                    )
                )
            ),
            components = Components(
                schemas = mapOf(
                    "CreatePet" to ReferenceOr.value(
                        Schema(
                            type = Schema.Type.Basic.Object,
                            required = listOf("name"),
                            properties = mapOf("name" to ReferenceOr.value(Schema.string))
                        )
                    )
                )
            )
        )

        val models = api.toApiTree("Pets").models
        assertEquals(
            listOf("CreatePet"),
            models.mapNotNull { (it as? Model.ContextHolder)?.context?.head as? NamingContext.Reference }
                .map(NamingContext.Reference::name)
        )
    }
}
