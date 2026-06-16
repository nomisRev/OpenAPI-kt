package io.github.nomisrev.render

import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.RequestBodyStrategy
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.classifyErrorStatus
import io.github.nomisrev.openapi.contentTypeToIdentifier
import io.github.nomisrev.openapi.contentTypeToMethodName
import io.github.nomisrev.openapi.contentTypeStrategy
import io.github.nomisrev.openapi.detectSignatureClashes
import io.github.nomisrev.openapi.toKotlinSignature
import io.github.nomisrev.openapi.toTypeName
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ContentTypeStrategyTest {
    private val config = RenderConfig(
        modelPackage = "io.github.nomisrev.render.test.model",
        apiPackage = "io.github.nomisrev.render.test.api",
        targets = setOf(KmpTarget.JVM),
    )

    @Test
    fun `content type naming examples`() {
        val cases = listOf(
            ContentType.parse("application/json; charset=utf-8") to "Json",
            ContentType.parse("application/xml") to "Xml",
            ContentType.parse("application/octet-stream") to "OctetStream",
            ContentType.parse("text/plain") to "TextPlain",
            ContentType.parse("image/png") to "ImagePng",
            ContentType.parse("application/sarif+json") to "SarifJson",
            ContentType.parse("application/vnd.github.v3.star+json") to "VndGithubV3StarJson",
            ContentType.parse("application/vnd.github.object") to "VndGithubObject",
            ContentType.parse("text/event-stream") to "TextEventStream",
        )

        cases.forEach { [contentType, expected] ->
            assertEquals(expected, contentTypeToIdentifier(contentType))
            assertEquals(expected.replaceFirstChar { it.lowercase() }, contentTypeToMethodName(contentType))
        }
    }

    @Test
    fun `content type naming collision handling appends numeric suffixes`() {
        assertEquals("Json2", contentTypeToIdentifier(ContentType.Application.Json, setOf("Json")))
        assertEquals("json2", contentTypeToMethodName(ContentType.Application.Json, setOf("json")))
        assertEquals(
            "VndGithubV3StarJson3",
            contentTypeToIdentifier(
                ContentType.parse("application/vnd.github.v3.star+json"),
                setOf("VndGithubV3StarJson", "VndGithubV3StarJson2"),
            ),
        )
    }

    @Test
    fun `response strategy ignores non-success statuses and deduplicates success content types`() {
        val returns = routeReturns(
            HttpStatusCode.OK to returnType(ContentType.Application.Json to stringModel()),
            HttpStatusCode.Created to returnType(ContentType.Application.Json to stringModel()),
            HttpStatusCode.NotFound to returnType(ContentType.parse("application/xml") to stringModel()),
        )

        assertEquals(io.github.nomisrev.openapi.ContentTypeStrategy.SingleContentType, returns.contentTypeStrategy())
    }

    @Test
    fun `response strategy switches to separate methods when success content types differ`() {
        val returns = routeReturns(
            HttpStatusCode.OK to returnType(ContentType.Application.Json to stringModel()),
            HttpStatusCode.Created to returnType(ContentType.parse("application/xml") to stringModel()),
            HttpStatusCode.NotFound to returnType(ContentType.parse("application/problem+json") to stringModel()),
        )

        assertEquals(
            io.github.nomisrev.openapi.ContentTypeStrategy.SeparateMethods(
                listOf(ContentType.Application.Json, ContentType.parse("application/xml")),
            ),
            returns.contentTypeStrategy(),
        )
    }

    @Test
    fun `error case classification covers no content single and multiple content types`() {
        assertEquals(
            io.github.nomisrev.openapi.ErrorCaseStrategy.NoContent,
            classifyErrorStatus(
                HttpStatusCode.NoContent,
                returnType(ContentType.Application.Json to stringModel()),
            ),
        )
        assertEquals(
            io.github.nomisrev.openapi.ErrorCaseStrategy.NoContent,
            classifyErrorStatus(
                HttpStatusCode.NotModified,
                returnType(ContentType.Application.Json to stringModel()),
            ),
        )
        assertEquals(
            io.github.nomisrev.openapi.ErrorCaseStrategy.SingleContentType(
                contentType = ContentType.Application.Json,
                body = Route.ReturnBody.Typed(stringModel()),
            ),
            classifyErrorStatus(
                HttpStatusCode.BadRequest,
                returnType(ContentType.Application.Json to stringModel()),
            ),
        )
        assertEquals(
            io.github.nomisrev.openapi.ErrorCaseStrategy.MultipleContentTypes(
                variants = listOf(
                    ContentType.Application.Json to Route.ReturnBody.Typed(stringModel()),
                    ContentType.parse("application/scim+json") to Route.ReturnBody.Typed(referenceModel("ScimError")),
                ),
            ),
            classifyErrorStatus(
                HttpStatusCode.BadRequest,
                returnType(
                    ContentType.Application.Json to stringModel(),
                    ContentType.parse("application/scim+json") to referenceModel("ScimError"),
                ),
            ),
        )
    }

    @Test
    fun `request body signatures are based on kotlin type names`() {
        val setBody = setBody(ContentType.Application.Json, stringModel())
        assertEquals(stringModel().toTypeName(config).toString(), setBody.toKotlinSignature(config))

        val formA = formUrlEncoded(
            formData("name", stringModel()),
            formData("count", intModel()),
        )
        val formB = formUrlEncoded(
            formData("count", intModel()),
            formData("name", stringModel()),
        )
        assertEquals("kotlin.Int|kotlin.String", formA.toKotlinSignature(config))
        assertEquals(formA.toKotlinSignature(config), formB.toKotlinSignature(config))

        val multipartA = multipartValue(
            formData("name", stringModel()),
            formData("user", referenceModel("User")),
        )
        val multipartB = multipartValue(
            formData("user", referenceModel("User")),
            formData("name", stringModel()),
        )
        assertEquals(multipartA.toKotlinSignature(config), multipartB.toKotlinSignature(config))

        val overloaded = overloadedBody()
        assertEquals(overloaded.type.toTypeName(config).toString(), overloaded.toKotlinSignature(config))
    }

    @Test
    fun `request body clash detection splits single overloads and clashing signatures`() {
        val singleBodies = bodies(
            ContentType.Application.Json to setBody(ContentType.Application.Json, stringModel()),
        )
        assertEquals(
            RequestBodyStrategy.Single(singleBodies.variants().single()),
            singleBodies.detectSignatureClashes(config),
        )

        val separateBodies = bodies(
            ContentType.Application.Json to setBody(ContentType.Application.Json, stringModel()),
            ContentType.parse("application/xml") to setBody(ContentType.parse("application/xml"), intModel()),
        )
        assertEquals(
            RequestBodyStrategy.SeparateOverloads(separateBodies.variants()),
            separateBodies.detectSignatureClashes(config),
        )

        val clashingBodies = bodies(
            ContentType.Text.Plain to setBody(ContentType.Text.Plain, stringModel()),
            ContentType.parse("text/x-markdown") to setBody(ContentType.parse("text/x-markdown"), stringModel()),
            ContentType.Application.Json to setBody(ContentType.Application.Json, referenceModel("User")),
        )
        val clashingVariants = clashingBodies.variants()
        assertEquals(
            RequestBodyStrategy.ClashingWithEnum(
                clashing = listOf(
                    clashingVariants[0],
                    clashingVariants[1],
                ),
                unique = listOf(clashingVariants[2]),
            ),
            clashingBodies.detectSignatureClashes(config),
        )
    }

    @Test
    fun `request body clash detection distinguishes optional form fields`() {
        val requiredBody = formUrlEncoded(
            formData("name", stringModel(), isRequired = true),
        )
        val optionalBody = formUrlEncoded(
            formData("name", stringModel(), isRequired = false),
        )
        val bodies = bodies(
            ContentType.Application.FormUrlEncoded to requiredBody,
            ContentType.parse("application/vnd.example.form") to optionalBody,
        )

        assertNotEquals(
            requiredBody.toKotlinSignature(config),
            optionalBody.toKotlinSignature(config),
        )
        assertEquals(
            RequestBodyStrategy.SeparateOverloads(bodies.variants()),
            bodies.detectSignatureClashes(config),
        )
    }

    private fun stringModel(): Model.Primitive.String =
        Model.Primitive.String(default = null, description = null, constraint = null, isNullable = false, title = null)

    private fun intModel(): Model.Primitive.Int =
        Model.Primitive.Int(default = null, description = null, constraint = null, isNullable = false, title = null)

    private fun referenceModel(name: String): Model.Reference =
        Model.Reference(
            context = NamingContext.reference(name, SchemaContext.Null),
            description = null,
            isNullable = false,
            title = null,
        )

    private fun overloadedBody(): Route.Body.OverloadedBody =
        Route.Body.OverloadedBody(
            contentType = ContentType.Application.Json,
            type = Model.OneOf(
                context = NamingContext.reference("Choice", SchemaContext.Null),
                cases = listOf(
                    Model.Union.Case(stringModel(), emptySet()),
                    Model.Union.Case(intModel(), emptySet()),
                ),
                default = null,
                description = null,
                title = null,
                dispatch = UnionDispatch.Structural,
                isNullable = false,
            ),
            description = null,
            extensions = emptyMap(),
        )

    private fun setBody(contentType: ContentType, model: Model): Route.Body.SetBody =
        Route.Body.SetBody(
            contentType = contentType,
            type = model,
            description = null,
            extensions = emptyMap(),
        )

    private fun formData(
        name: String,
        type: Model,
        isRequired: Boolean = true,
    ): Route.Body.Multipart.FormData =
        Route.Body.Multipart.FormData(
            name = name,
            type = type,
            contentType = null,
            isRequired = isRequired,
        )

    private fun formUrlEncoded(vararg parameters: Route.Body.Multipart.FormData): Route.Body.FormUrlEncoded =
        Route.Body.FormUrlEncoded(
            parameters = parameters.toList(),
            description = null,
            extensions = emptyMap(),
        )

    private fun multipartValue(vararg parameters: Route.Body.Multipart.FormData): Route.Body.Multipart.Value =
        Route.Body.Multipart.Value(
            parameters = parameters.toList(),
            description = null,
            extensions = emptyMap(),
        )

    private fun bodies(vararg entries: Pair<ContentType, Route.Body>): Route.Bodies =
        Route.Bodies(
            required = true,
            types = mapOf(*entries),
            extensions = emptyMap(),
        )

    private fun returnType(vararg entries: Pair<ContentType, Model>): Route.ReturnType =
        Route.ReturnType(
            types = mapOf(*entries),
            extensions = emptyMap(),
        )

    private fun routeReturns(vararg entries: Pair<HttpStatusCode, Route.ReturnType>): Route.Returns =
        Route.Returns(
            default = null,
            responses = mapOf(*entries),
            extensions = emptyMap(),
        )
}
