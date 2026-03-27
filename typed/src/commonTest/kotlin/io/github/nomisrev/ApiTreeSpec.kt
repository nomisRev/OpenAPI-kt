package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.OpenApiPreprocessors
import io.github.nomisrev.openapi.PathNode
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.buildTree
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.toApiTree
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
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
    fun fixed(wireValue: String, sourceName: String = "workflowId"): PathSegment =
        PathSegment.FixedValue(wireValue, sourceName)
    fun param(name: String): PathSegment = PathSegment.Parameter(name, stringType)
    fun intParam(name: String): PathSegment = PathSegment.Parameter(name, intType)
    fun scalarWrapper(name: String, model: Model): Model.Object =
        Model.Object.value(
            context = NamingContext.Reference(name, SchemaContext.Null),
            property = model,
            isScalarWrapper = true,
        )

    fun objectWrapper(name: String, model: Model): Model.Object =
        Model.Object.value(
            context = NamingContext.Reference(name, SchemaContext.Null),
            property = model,
        )

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
        val unionType = Model.OneOf(
            context = NamingContext.path(listOf("workflows", routeKey)),
            cases = listOf(
                Model.Union.Case(intType, emptySet()),
                Model.Union.Case(enumType, emptySet()),
            ),
            default = null,
            description = null,
            title = null,
            dispatch = UnionDispatch.Structural,
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

    test("buildTree merges FixedValue with hyphen-underscore equivalent Literal into one node") {
        // Simulates the GitHub case where `/orgs/{org}/{security_product}/{enablement}` is expanded
        // into FixedValue("secret_scanning") but `/orgs/{org}/secret-scanning/alerts` produces
        // Literal("secret-scanning"). The two names are separator-equivalent and must share one node.
        val getAlerts = route(
            HttpMethod.Get,
            literal("orgs"),
            param("org"),
            literal("secret-scanning"),
            literal("alerts"),
        )
        val postEnableAll = route(
            HttpMethod.Post,
            literal("orgs"),
            param("org"),
            fixed("secret_scanning", "security_product"),
            fixed("enable_all", "enablement"),
        )

        val tree = listOf(getAlerts, postEnableAll).buildTree("GitHub")
        val orgsNode = tree.children.single()
        val orgParamNode = orgsNode.children.single()
        // Both routes must share a single "secret-scanning / secret_scanning" node.
        assertEquals(1, orgParamNode.children.size, "Expected one merged secret-scanning node")
        val secretScanningNode = orgParamNode.children.single()
        val fixedSegment = assertIs<PathSegment.FixedValue>(secretScanningNode.segment)
        assertEquals("secret_scanning", fixedSegment.wireValue)
        val childNames = secretScanningNode.children.map { it.segment.name }.toSet()
        assertEquals(setOf("alerts", "enable_all"), childNames)
    }

    test("buildTree prefers fixed-value representation when merged with a literal sibling node") {
        val getRuns = route(HttpMethod.Get, literal("workflows"), fixed("queued"), literal("runs"))
        val getSummary = route(HttpMethod.Get, literal("workflows"), literal("queued"), literal("summary"))

        val mergedNode = listOf(getRuns, getSummary)
            .buildTree("Workflows")
            .children
            .single()
            .children
            .single()

        val segment = assertIs<PathSegment.FixedValue>(mergedNode.segment)
        assertEquals("queued", segment.wireValue)
        assertEquals(setOf("runs", "summary"), mergedNode.children.map { it.segment.name }.toSet())
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

    test("reproducer: shared multi-enum overloaded path node conflicts when case order differs") {
        fun overloadedMultiEnumWorkflowParam(
            name: String,
            routeKey: String,
            enumCases: List<List<String>>,
        ): PathSegment.OverloadedParameter {
            val unionType = Model.OneOf(
                context = NamingContext.path(listOf("workflows", routeKey)),
                cases = enumCases.mapIndexed { index, values ->
                    val enumType = Model.Enum(
                        context = NamingContext.path(listOf("workflows", routeKey, "workflowId", "case$index")),
                        inner = stringType,
                        values = values,
                        default = null,
                        description = null,
                        title = null,
                        isNullable = false,
                    )
                    Model.Union.Case(enumType, emptySet())
                },
                default = null,
                description = null,
                title = null,
                dispatch = UnionDispatch.Structural,
                isNullable = false,
            )
            return PathSegment.OverloadedParameter(name, unionType)
        }

        val getRuns = route(
            HttpMethod.Get,
            literal("workflows"),
            overloadedMultiEnumWorkflowParam(
                name = "workflowId",
                routeKey = "runs",
                enumCases = listOf(listOf("queued"), listOf("in-progress")),
            ),
            literal("runs"),
        )
        val getHistory = route(
            HttpMethod.Get,
            literal("workflows"),
            overloadedMultiEnumWorkflowParam(
                name = "workflowId",
                routeKey = "history",
                enumCases = listOf(listOf("in-progress"), listOf("queued")),
            ),
            literal("history"),
        )

        assertSharedPathParameterConflict(
            routes = listOf(getRuns, getHistory),
            expectedNode = "/workflows/{workflowId}",
            "GET /workflows/{workflowId}/history",
            "GET /workflows/{workflowId}/runs",
            "OverloadedParameter(name=workflowId, type=OneOf(cases=[Enum(values=[in-progress]), Enum(values=[queued])]))",
            "OverloadedParameter(name=workflowId, type=OneOf(cases=[Enum(values=[queued]), Enum(values=[in-progress])]))",
        )
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
            "OverloadedParameter(name=workflowId, type=OneOf(cases=[Int, Enum(values=[queued, in-progress])]))",
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

    test("buildTree rejects different shared path parameter flattening shapes") {
        val flattenedPet = route(
            HttpMethod.Get,
            literal("pets"),
            PathSegment.Parameter("petId", scalarWrapper("PetId", intType)),
        )
        val wrappedPet = route(
            HttpMethod.Delete,
            literal("pets"),
            PathSegment.Parameter("petId", objectWrapper("PetIdObject", intType)),
        )

        assertSharedPathParameterConflict(
            routes = listOf(flattenedPet, wrappedPet),
            expectedNode = "/pets/{petId}",
            "DELETE /pets/{petId}",
            "GET /pets/{petId}",
            "Parameter(name=petId, model=Object(properties=[value:Int]))",
            "Parameter(name=petId, model=ScalarWrapper(properties=[value:Int]))",
        )
    }

    test("toApiTree rejects conflicting explicit literal and unrolled enum literal routes") {
        val enumPathParameter = ReferenceOr.value(
            Parameter(
                name = "workflowId",
                input = Parameter.Input.Path,
                schema = ReferenceOr.value(Schema.string.copy(enum = listOf("queued", "in-progress"))),
            )
        )
        val api = OpenAPI(
            info = Info("Workflows", "1.0"),
            paths = mapOf(
                "/workflows/{workflowId}/runs" to PathItem(
                    get = Operation(
                        parameters = listOf(enumPathParameter),
                        responses = Responses(200, Response())
                    )
                ),
                "/workflows/queued/runs" to PathItem(
                    get = Operation(
                        responses = Responses(204, Response())
                    )
                ),
            ),
        )

        val error = assertFailsWith<IllegalArgumentException> {
            api.toApiTree("Workflows")
        }
        val message = requireNotNull(error.message)
        assertTrue(
            message.startsWith("Conflicting concrete route 'GET /workflows/queued/runs': "),
            message,
        )
        assertTrue(message.contains("200:[]"), message)
        assertTrue(message.contains("204:[]"), message)
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

    test("toApiTree applies OpenAPI preprocessors before building routes") {
        val api = OpenAPI(
            info = Info("Test", "1.0"),
            paths = mapOf(
                "/pets" to PathItem(
                    get = Operation(
                        responses = Responses(200, Response())
                    )
                ),
                "/responses/{response_id}/input_items" to PathItem(
                    get = Operation(
                        responses = Responses(200, Response())
                    )
                ),
            ),
        )

        val tree = api.toApiTree(
            name = "Pets",
            preprocessor = OpenApiPreprocessors.excludePaths(
                setOf("/responses/{response_id}/input_items")
            ),
        )

        assertEquals("Pets", tree.name)
        assertEquals(listOf("pets"), tree.children.map { (it.segment as PathSegment.Literal).name })
        assertTrue(tree.children.single().operations.containsKey(HttpMethod.Get))
    }

    test("toApiTree does not keep referenced enum models that are fully unrolled from path params") {
        val api = OpenAPI(
            info = Info("Test", "1.0"),
            paths = mapOf(
                "/workflows/{workflowId}" to PathItem(
                    get = Operation(
                        parameters = listOf(
                            ReferenceOr.value(
                                Parameter(
                                    name = "workflowId",
                                    input = Parameter.Input.Path,
                                    schema = ReferenceOr.schema("WorkflowState"),
                                )
                            )
                        ),
                        responses = Responses(200, Response())
                    )
                )
            ),
            components = Components(
                schemas = mapOf(
                    "WorkflowState" to ReferenceOr.value(
                        Schema.string.copy(enum = listOf("queued", "in-progress"))
                    )
                )
            )
        )

        assertTrue(api.toApiTree("Workflows").models.isEmpty())
    }
}
