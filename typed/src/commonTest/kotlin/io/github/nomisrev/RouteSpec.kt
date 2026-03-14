package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.toRoutes
import io.ktor.http.HttpMethod
import kotlin.test.assertNotEquals

val routeSpec by testSuite {
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

    fun pathParameter(name: String, schema: Schema): ReferenceOr<Parameter> = ReferenceOr.value(
        Parameter(
            name = name,
            input = Parameter.Input.Path,
            schema = ReferenceOr.value(schema)
        )
    )

    fun openAPI(
        path: String,
        parameters: List<ReferenceOr<Parameter>> = emptyList(),
        pathParameters: List<ReferenceOr<Parameter>> = emptyList(),
        components: Components = Components(),
    ): OpenAPI = OpenAPI(
        info = Info("Test", "1.0"),
        paths = mapOf(
            path to PathItem(
                get = Operation(
                    parameters = parameters,
                    responses = Responses(200, Response())
                ),
                parameters = pathParameters,
            )
        ),
        components = components,
    )

    suspend fun routes(api: OpenAPI) =
        Registry(api).use { registry -> with(registry) { api.toRoutes() } }

    test("NamingContext path uniqueness by method") {
        val segments = listOf(PathSegment.Literal("pets"))
        val get = NamingContext.path(segments, HttpMethod.Get).nest(NamingContext.Response)
        val post = NamingContext.path(segments, HttpMethod.Post).nest(NamingContext.Response)
        assertNotEquals(get, post)
    }

    test("NamingContext path uniqueness by literal path") {
        val pets = NamingContext.path(listOf(PathSegment.Literal("pets")), HttpMethod.Get).nest(NamingContext.Response)
        val users = NamingContext.path(listOf(PathSegment.Literal("users")), HttpMethod.Get).nest(NamingContext.Response)
        assertNotEquals(pets, users)
    }

    test("NamingContext path uniqueness by parameter name") {
        val byPet = NamingContext.path(
            listOf(PathSegment.Literal("resources"), PathSegment.Parameter("petId", stringType)),
            HttpMethod.Get
        ).nest(NamingContext.Response)
        val byUser = NamingContext.path(
            listOf(PathSegment.Literal("resources"), PathSegment.Parameter("userId", stringType)),
            HttpMethod.Get
        ).nest(NamingContext.Response)
        assertNotEquals(byPet, byUser)
    }

    data class RouteSegmentsCase(
        val api: OpenAPI,
        val expected: List<PathSegment>,
    )

    val componentParam = Parameter(
        name = "petId",
        input = Parameter.Input.Path,
        schema = ReferenceOr.value(Schema.integer.copy(format = "int32"))
    )

    listOf(
        RouteSegmentsCase(
            api = openAPI(path = "/pets"),
            expected = listOf(PathSegment.Literal("pets")),
        ),
        RouteSegmentsCase(
            api = openAPI(path = "/pets/{petId}"),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", stringType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(
                path = "/pets/{petId}",
                parameters = listOf(pathParameter("petId", Schema.integer.copy(format = "int32"))),
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(
                path = "/pets/{petId}",
                parameters = listOf(ReferenceOr.Reference("#/components/parameters/PetId")),
                components = Components(
                    parameters = mapOf("PetId" to ReferenceOr.value(componentParam))
                ),
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(
                path = "/pets/{petId}",
                pathParameters = listOf(pathParameter("petId", Schema.integer.copy(format = "int32"))),
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(path = "/repos/{owner}/{repo}/collaborators"),
            expected = listOf(
                PathSegment.Literal("repos"),
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType),
                PathSegment.Literal("collaborators")
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(path = "/{owner}/{repo}"),
            expected = listOf(
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType)
            ),
        ),
    ).verifyAll("toRoutes segments") { input ->
        Eq(input.expected, routes(input.api).single().segments)
    }
}
