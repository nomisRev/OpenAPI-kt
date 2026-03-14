package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.PathNode
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.buildTree
import io.github.nomisrev.openapi.routes.Route
import io.ktor.http.HttpMethod
import kotlin.test.assertEquals

val apiTreeSpec by testSuite {
    val stringType = Model.Primitive.String(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null,
    )

    fun literal(name: String): PathSegment = PathSegment.Literal(name)
    fun param(name: String): PathSegment = PathSegment.Parameter(name, stringType)

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
}
