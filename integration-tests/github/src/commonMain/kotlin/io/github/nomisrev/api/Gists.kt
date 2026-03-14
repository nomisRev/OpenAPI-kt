package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.Required
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import io.github.nomisrev.model.BaseGist
import io.github.nomisrev.model.BasicError
import kotlinx.datetime.LocalDateTime
import io.github.nomisrev.model.GistSimple
import io.github.nomisrev.model.ValidationError
import io.github.nomisrev.model.GistComment
import io.github.nomisrev.model.GistCommit
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.contentType

interface Gists {
    val public: Gists.Public

    val starred: Gists.Starred

    val comments: Gists.Comments

    val commits: Gists.Commits

    val forks: Gists.Forks

    val star: Gists.Star

    @Serializable
    data class GistsCreateBody(
        val description: String? = null,
        @Required val files: List<Files>,
        val public: Public? = null,
    ) {
        @Serializable
        @JvmInline
        value class Files(val content: String)

        @Serializable(with = Public.Serializer::class)
        sealed interface Public {
            @Serializable
            @JvmInline
            value class CaseBoolean(val value: Boolean) : Public

            @Serializable
            enum class TrueOrFalse : Public {
                @SerialName("true") True, @SerialName("false") False;
            }

            object Serializer : KSerializer<Public> {
                @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                override val descriptor: SerialDescriptor =
                    buildSerialDescriptor("io.github.nomisrev.api.Gists.GistsCreateBody.Public", PolymorphicKind.SEALED) {
                        element("CaseBoolean", Boolean.serializer().descriptor)
                        element("TrueOrFalse", GistsCreateBody.Public.TrueOrFalse.serializer().descriptor)
                    }

                override fun deserialize(decoder: Decoder): Public {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                        value,
                        TrueOrFalse::class to { decodeFromJsonElement(GistsCreateBody.Public.TrueOrFalse.serializer(), it) },
                        CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
                    )
                }

                override fun serialize(encoder: Encoder, value: Public) = when(value) {
                    is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
                    is TrueOrFalse -> encoder.encodeSerializableValue(GistsCreateBody.Public.TrueOrFalse.serializer(), value)
                }
            }
        }
    }


    @Serializable
    data class GistsGetResponse(
        val block: Block? = null,
        val message: String? = null,
        @SerialName("documentation_url") val documentationUrl: String? = null,
    ) {
        @Serializable
        data class Block(
            val reason: String? = null,
            @SerialName("created_at") val createdAt: String? = null,
            @SerialName("html_url") val htmlUrl: String? = null,
        )
    }


    @Serializable
    data class GistsUpdateBody(val description: String? = null, val files: List<Files>? = null) {
        @Serializable
        data class Files(val content: String? = null, val filename: String? = null)
    }

    sealed interface GistsListResult {
        data class OK(val value: List<BaseGist>) : GistsListResult

        data object NotModified : GistsListResult

        data class Forbidden(val value: BasicError) : GistsListResult
    }

    suspend fun gistsList(
        page: Long = 1L,
        perPage: Long = 30L,
        since: LocalDateTime? = null,
    ): GistsListResult

    sealed interface GistsCreateResult {
        data class Created(val value: GistSimple) : GistsCreateResult

        data object NotModified : GistsCreateResult

        data class Forbidden(val value: BasicError) : GistsCreateResult

        data class NotFound(val value: BasicError) : GistsCreateResult

        data class UnprocessableEntity(val value: ValidationError) : GistsCreateResult
    }

    suspend fun gistsCreate(
        body: GistsCreateBody,
    ): GistsCreateResult

    sealed interface GistsGetResult {
        data class OK(val value: GistSimple) : GistsGetResult

        data object NotModified : GistsGetResult

        data class Forbidden(val value: GistsGetResponse) : GistsGetResult

        data class NotFound(val value: BasicError) : GistsGetResult
    }

    suspend fun gistsGet(
        gistId: String,
    ): GistsGetResult

    sealed interface GistsDeleteResult {
        data object NoContent : GistsDeleteResult

        data object NotModified : GistsDeleteResult

        data class Forbidden(val value: BasicError) : GistsDeleteResult

        data class NotFound(val value: BasicError) : GistsDeleteResult
    }

    suspend fun gistsDelete(
        gistId: String,
    ): GistsDeleteResult

    sealed interface GistsUpdateResult {
        data class OK(val value: GistSimple) : GistsUpdateResult

        data class NotFound(val value: BasicError) : GistsUpdateResult

        data class UnprocessableEntity(val value: ValidationError) : GistsUpdateResult
    }

    suspend fun gistsUpdate(
        gistId: String,
        body: GistsUpdateBody,
    ): GistsUpdateResult

    sealed interface GistsGetRevisionResult {
        data class OK(val value: GistSimple) : GistsGetRevisionResult

        data class Forbidden(val value: BasicError) : GistsGetRevisionResult

        data class NotFound(val value: BasicError) : GistsGetRevisionResult

        data class UnprocessableEntity(val value: ValidationError) : GistsGetRevisionResult
    }

    suspend fun gistsGetRevision(
        gistId: String,
        sha: String,
    ): GistsGetRevisionResult

    interface Public {
        sealed interface GistsListPublicResult {
            data class OK(val value: List<BaseGist>) : GistsListPublicResult

            data object NotModified : GistsListPublicResult

            data class Forbidden(val value: BasicError) : GistsListPublicResult

            data class UnprocessableEntity(val value: ValidationError) : GistsListPublicResult
        }

        suspend fun gistsListPublic(
            page: Long = 1L,
            perPage: Long = 30L,
            since: LocalDateTime? = null,
        ): GistsListPublicResult
    }

    interface Starred {
        sealed interface GistsListStarredResult {
            data class OK(val value: List<BaseGist>) : GistsListStarredResult

            data object NotModified : GistsListStarredResult

            data class Unauthorized(val value: BasicError) : GistsListStarredResult

            data class Forbidden(val value: BasicError) : GistsListStarredResult
        }

        suspend fun gistsListStarred(
            page: Long = 1L,
            perPage: Long = 30L,
            since: LocalDateTime? = null,
        ): GistsListStarredResult
    }

    interface Comments {
        @Serializable
        @JvmInline
        value class GistsCreateCommentBody(val body: String)


        @Serializable
        data class GistsGetCommentResponse(
            val block: Block? = null,
            val message: String? = null,
            @SerialName("documentation_url") val documentationUrl: String? = null,
        ) {
            @Serializable
            data class Block(
                val reason: String? = null,
                @SerialName("created_at") val createdAt: String? = null,
                @SerialName("html_url") val htmlUrl: String? = null,
            )
        }


        @Serializable
        @JvmInline
        value class GistsUpdateCommentBody(val body: String)

        sealed interface GistsListCommentsResult {
            data class OK(val value: List<GistComment>) : GistsListCommentsResult

            data object NotModified : GistsListCommentsResult

            data class Forbidden(val value: BasicError) : GistsListCommentsResult

            data class NotFound(val value: BasicError) : GistsListCommentsResult
        }

        suspend fun gistsListComments(
            gistId: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): GistsListCommentsResult

        sealed interface GistsCreateCommentResult {
            data class Created(val value: GistComment) : GistsCreateCommentResult

            data object NotModified : GistsCreateCommentResult

            data class Forbidden(val value: BasicError) : GistsCreateCommentResult

            data class NotFound(val value: BasicError) : GistsCreateCommentResult
        }

        suspend fun gistsCreateComment(
            gistId: String,
            body: GistsCreateCommentBody,
        ): GistsCreateCommentResult

        sealed interface GistsGetCommentResult {
            data class OK(val value: GistComment) : GistsGetCommentResult

            data object NotModified : GistsGetCommentResult

            data class Forbidden(val value: GistsGetCommentResponse) : GistsGetCommentResult

            data class NotFound(val value: BasicError) : GistsGetCommentResult
        }

        suspend fun gistsGetComment(
            gistId: String,
            commentId: Long,
        ): GistsGetCommentResult

        sealed interface GistsDeleteCommentResult {
            data object NoContent : GistsDeleteCommentResult

            data object NotModified : GistsDeleteCommentResult

            data class Forbidden(val value: BasicError) : GistsDeleteCommentResult

            data class NotFound(val value: BasicError) : GistsDeleteCommentResult
        }

        suspend fun gistsDeleteComment(
            gistId: String,
            commentId: Long,
        ): GistsDeleteCommentResult

        sealed interface GistsUpdateCommentResult {
            data class OK(val value: GistComment) : GistsUpdateCommentResult

            data class NotFound(val value: BasicError) : GistsUpdateCommentResult
        }

        suspend fun gistsUpdateComment(
            gistId: String,
            commentId: Long,
            body: GistsUpdateCommentBody,
        ): GistsUpdateCommentResult
    }

    interface Commits {
        sealed interface GistsListCommitsResult {
            data class OK(val value: List<GistCommit>) : GistsListCommitsResult

            data object NotModified : GistsListCommitsResult

            data class Forbidden(val value: BasicError) : GistsListCommitsResult

            data class NotFound(val value: BasicError) : GistsListCommitsResult
        }

        suspend fun gistsListCommits(
            gistId: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): GistsListCommitsResult
    }

    interface Forks {
        sealed interface GistsListForksResult {
            data class OK(val value: List<GistSimple>) : GistsListForksResult

            data object NotModified : GistsListForksResult

            data class Forbidden(val value: BasicError) : GistsListForksResult

            data class NotFound(val value: BasicError) : GistsListForksResult
        }

        suspend fun gistsListForks(
            gistId: String,
            page: Long = 1L,
            perPage: Long = 30L,
        ): GistsListForksResult

        sealed interface GistsForkResult {
            data class Created(val value: BaseGist) : GistsForkResult

            data object NotModified : GistsForkResult

            data class Forbidden(val value: BasicError) : GistsForkResult

            data class NotFound(val value: BasicError) : GistsForkResult

            data class UnprocessableEntity(val value: ValidationError) : GistsForkResult
        }

        suspend fun gistsFork(
            gistId: String,
        ): GistsForkResult
    }

    interface Star {
        sealed interface GistsCheckIsStarredResult {
            data object NoContent : GistsCheckIsStarredResult

            data object NotModified : GistsCheckIsStarredResult

            data class Forbidden(val value: BasicError) : GistsCheckIsStarredResult

            data object NotFound : GistsCheckIsStarredResult
        }

        suspend fun gistsCheckIsStarred(
            gistId: String,
        ): GistsCheckIsStarredResult

        sealed interface GistsStarResult {
            data object NoContent : GistsStarResult

            data object NotModified : GistsStarResult

            data class Forbidden(val value: BasicError) : GistsStarResult

            data class NotFound(val value: BasicError) : GistsStarResult
        }

        suspend fun gistsStar(
            gistId: String,
        ): GistsStarResult

        sealed interface GistsUnstarResult {
            data object NoContent : GistsUnstarResult

            data object NotModified : GistsUnstarResult

            data class Forbidden(val value: BasicError) : GistsUnstarResult

            data class NotFound(val value: BasicError) : GistsUnstarResult
        }

        suspend fun gistsUnstar(
            gistId: String,
        ): GistsUnstarResult
    }
}

internal class KtorGists(private val client: HttpClient) : Gists {
    override val public: Gists.Public = KtorGistsPublic(client)

    override val starred: Gists.Starred = KtorGistsStarred(client)

    override val comments: Gists.Comments = KtorGistsComments(client)

    override val commits: Gists.Commits = KtorGistsCommits(client)

    override val forks: Gists.Forks = KtorGistsForks(client)

    override val star: Gists.Star = KtorGistsStar(client)

    override suspend fun gistsList(page: Long, perPage: Long, since: LocalDateTime?): Gists.GistsListResult {
        val response = client.get("/gists") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.GistsListResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.GistsListResult.NotModified
            HttpStatusCode.Forbidden -> Gists.GistsListResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsCreate(body: Gists.GistsCreateBody): Gists.GistsCreateResult {
        val response = client.post("/gists") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Gists.GistsCreateResult.Created(response.body())
            HttpStatusCode.NotModified -> Gists.GistsCreateResult.NotModified
            HttpStatusCode.Forbidden -> Gists.GistsCreateResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.GistsCreateResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Gists.GistsCreateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsGet(gistId: String): Gists.GistsGetResult {
        val response = client.get("/gists/$gistId")
        return when (response.status) {
            HttpStatusCode.OK -> Gists.GistsGetResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.GistsGetResult.NotModified
            HttpStatusCode.Forbidden -> Gists.GistsGetResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.GistsGetResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsDelete(gistId: String): Gists.GistsDeleteResult {
        val response = client.delete("/gists/$gistId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Gists.GistsDeleteResult.NoContent
            HttpStatusCode.NotModified -> Gists.GistsDeleteResult.NotModified
            HttpStatusCode.Forbidden -> Gists.GistsDeleteResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.GistsDeleteResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsUpdate(gistId: String, body: Gists.GistsUpdateBody): Gists.GistsUpdateResult {
        val response = client.patch("/gists/$gistId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.GistsUpdateResult.OK(response.body())
            HttpStatusCode.NotFound -> Gists.GistsUpdateResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Gists.GistsUpdateResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsGetRevision(gistId: String, sha: String): Gists.GistsGetRevisionResult {
        val response = client.get("/gists/$gistId/$sha")
        return when (response.status) {
            HttpStatusCode.OK -> Gists.GistsGetRevisionResult.OK(response.body())
            HttpStatusCode.Forbidden -> Gists.GistsGetRevisionResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.GistsGetRevisionResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Gists.GistsGetRevisionResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorGistsPublic(private val client: HttpClient) : Gists.Public {
    override suspend fun gistsListPublic(page: Long, perPage: Long, since: LocalDateTime?): Gists.Public.GistsListPublicResult {
        val response = client.get("/gists/public") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Public.GistsListPublicResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.Public.GistsListPublicResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Public.GistsListPublicResult.Forbidden(response.body())
            HttpStatusCode.UnprocessableEntity -> Gists.Public.GistsListPublicResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorGistsStarred(private val client: HttpClient) : Gists.Starred {
    override suspend fun gistsListStarred(page: Long, perPage: Long, since: LocalDateTime?): Gists.Starred.GistsListStarredResult {
        val response = client.get("/gists/starred") {
            parameter("page", page)
            parameter("per_page", perPage)
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Starred.GistsListStarredResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.Starred.GistsListStarredResult.NotModified
            HttpStatusCode.Unauthorized -> Gists.Starred.GistsListStarredResult.Unauthorized(response.body())
            HttpStatusCode.Forbidden -> Gists.Starred.GistsListStarredResult.Forbidden(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorGistsComments(private val client: HttpClient) : Gists.Comments {
    override suspend fun gistsListComments(gistId: String, page: Long, perPage: Long): Gists.Comments.GistsListCommentsResult {
        val response = client.get("/gists/$gistId/comments") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Comments.GistsListCommentsResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.Comments.GistsListCommentsResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Comments.GistsListCommentsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Comments.GistsListCommentsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsCreateComment(gistId: String, body: Gists.Comments.GistsCreateCommentBody): Gists.Comments.GistsCreateCommentResult {
        val response = client.post("/gists/$gistId/comments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.Created -> Gists.Comments.GistsCreateCommentResult.Created(response.body())
            HttpStatusCode.NotModified -> Gists.Comments.GistsCreateCommentResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Comments.GistsCreateCommentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Comments.GistsCreateCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsGetComment(gistId: String, commentId: Long): Gists.Comments.GistsGetCommentResult {
        val response = client.get("/gists/$gistId/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Comments.GistsGetCommentResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.Comments.GistsGetCommentResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Comments.GistsGetCommentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Comments.GistsGetCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsDeleteComment(gistId: String, commentId: Long): Gists.Comments.GistsDeleteCommentResult {
        val response = client.delete("/gists/$gistId/comments/$commentId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Gists.Comments.GistsDeleteCommentResult.NoContent
            HttpStatusCode.NotModified -> Gists.Comments.GistsDeleteCommentResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Comments.GistsDeleteCommentResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Comments.GistsDeleteCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsUpdateComment(gistId: String, commentId: Long, body: Gists.Comments.GistsUpdateCommentBody): Gists.Comments.GistsUpdateCommentResult {
        val response = client.patch("/gists/$gistId/comments/$commentId") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Comments.GistsUpdateCommentResult.OK(response.body())
            HttpStatusCode.NotFound -> Gists.Comments.GistsUpdateCommentResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorGistsCommits(private val client: HttpClient) : Gists.Commits {
    override suspend fun gistsListCommits(gistId: String, page: Long, perPage: Long): Gists.Commits.GistsListCommitsResult {
        val response = client.get("/gists/$gistId/commits") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Commits.GistsListCommitsResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.Commits.GistsListCommitsResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Commits.GistsListCommitsResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Commits.GistsListCommitsResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorGistsForks(private val client: HttpClient) : Gists.Forks {
    override suspend fun gistsListForks(gistId: String, page: Long, perPage: Long): Gists.Forks.GistsListForksResult {
        val response = client.get("/gists/$gistId/forks") {
            parameter("page", page)
            parameter("per_page", perPage)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Gists.Forks.GistsListForksResult.OK(response.body())
            HttpStatusCode.NotModified -> Gists.Forks.GistsListForksResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Forks.GistsListForksResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Forks.GistsListForksResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsFork(gistId: String): Gists.Forks.GistsForkResult {
        val response = client.post("/gists/$gistId/forks")
        return when (response.status) {
            HttpStatusCode.Created -> Gists.Forks.GistsForkResult.Created(response.body())
            HttpStatusCode.NotModified -> Gists.Forks.GistsForkResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Forks.GistsForkResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Forks.GistsForkResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Gists.Forks.GistsForkResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorGistsStar(private val client: HttpClient) : Gists.Star {
    override suspend fun gistsCheckIsStarred(gistId: String): Gists.Star.GistsCheckIsStarredResult {
        val response = client.get("/gists/$gistId/star")
        return when (response.status) {
            HttpStatusCode.NoContent -> Gists.Star.GistsCheckIsStarredResult.NoContent
            HttpStatusCode.NotModified -> Gists.Star.GistsCheckIsStarredResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Star.GistsCheckIsStarredResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Star.GistsCheckIsStarredResult.NotFound
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsStar(gistId: String): Gists.Star.GistsStarResult {
        val response = client.put("/gists/$gistId/star")
        return when (response.status) {
            HttpStatusCode.NoContent -> Gists.Star.GistsStarResult.NoContent
            HttpStatusCode.NotModified -> Gists.Star.GistsStarResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Star.GistsStarResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Star.GistsStarResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun gistsUnstar(gistId: String): Gists.Star.GistsUnstarResult {
        val response = client.delete("/gists/$gistId/star")
        return when (response.status) {
            HttpStatusCode.NoContent -> Gists.Star.GistsUnstarResult.NoContent
            HttpStatusCode.NotModified -> Gists.Star.GistsUnstarResult.NotModified
            HttpStatusCode.Forbidden -> Gists.Star.GistsUnstarResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Gists.Star.GistsUnstarResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
