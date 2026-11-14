package io.github.api

import io.github.model.BaseGist
import io.github.model.BasicError
import io.github.model.GistComment
import io.github.model.GistCommit
import io.github.model.GistSimple
import io.github.model.Optional
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull

public class Gists internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public val post: Post = Post(client)

  public val `public`: Public = Public(client)

  public val starred: Starred = Starred(client)

  public fun gistId(gistId: String): GistIdPath = GistIdPath(client, gistId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      since: Instant? = null,
      perPage: Long? = 30L,
      page: Long? = 1L,
    ): Response {
      val response = client.get("gists") {
        if (since != null) {
          this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
        }
        if (perPage != null) {
          this.url.encodedParameters.append("per_page", perPage.toString())
        }
        if (page != null) {
          this.url.encodedParameters.append("page", page.toString())
        }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        403 -> Response.Forbidden(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<BaseGist>,
      ) : Response

      public data object NotModified : Response

      public data class Forbidden(
        public val `value`: BasicError,
      ) : Response
    }
  }

  public class Post internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(
      description: String? = null,
      files: Map<String, Files>,
      `public`: Public? = null,
    ): Response {
      val response = client.post("gists") {
        contentType(ContentType.Application.Json)
        setBody(Body(description = description, files = files, public = public))
      }
      return when (response.status.value) {
        201 -> Response.Created(response.body())
        304 -> Response.NotModified
        403 -> Response.Forbidden(response.body())
        404 -> Response.NotFound(response.body())
        422 -> Response.UnprocessableEntity(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    @Serializable
    public data class Files(
      public val content: String,
    )

    @Serializable(with = Public.Serializer::class)
    public sealed interface Public {
      @Serializable
      @JvmInline
      public value class CaseBoolean(
        public val `value`: Boolean,
      ) : Public

      @Serializable
      public enum class TrueOrFalse(
        public val `value`: String,
      ) : Public {
        @SerialName("true")
        True("true"),
        @SerialName("false")
        False("false"),
        ;
      }

      public object Serializer : KSerializer<Public> {
        @OptIn(
          InternalSerializationApi::class,
          ExperimentalSerializationApi::class,
        )
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("io.github.api.Gists.Post.Public", PolymorphicKind.SEALED) {
          element("CaseBoolean", Boolean.serializer().descriptor)
          element("TrueOrFalse", TrueOrFalse.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): Public {
          val value = decoder.decodeSerializableValue(JsonElement.serializer())
          val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
          return when {
            value is JsonPrimitive && value.isString -> json.attemptDeserialize(
              value,
              TrueOrFalse::class to { decodeFromJsonElement(TrueOrFalse.serializer(), it) },
            )
            value is JsonPrimitive && !value.isString && value.booleanOrNull != null -> json.attemptDeserialize(
              value,
              CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
            )
            else -> throw UnionSerializationException(value, emptyMap())
          }
        }

        override fun serialize(encoder: Encoder, `value`: Public) {
          when(value) {
            is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
            is TrueOrFalse -> encoder.encodeSerializableValue(TrueOrFalse.serializer(), value)
          }
        }
      }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    internal data class Body(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val description: String? = null,
      public val files: Map<String, Files>,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val `public`: Public? = null,
    )

    public sealed interface Response {
      public data class Created(
        public val `value`: GistSimple,
      ) : Response

      public data object NotModified : Response

      public data class Forbidden(
        public val `value`: BasicError,
      ) : Response

      public data class NotFound(
        public val `value`: BasicError,
      ) : Response

      public data class UnprocessableEntity(
        public val `value`: ValidationError,
      ) : Response
    }
  }

  public class Public internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        since: Instant? = null,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("gists/public") {
          if (since != null) {
            this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          403 -> Response.Forbidden(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<BaseGist>,
        ) : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response
      }
    }
  }

  public class Starred internal constructor(
    private val client: HttpClient,
  ) {
    public val `get`: Get = Get(client)

    public class Get internal constructor(
      private val client: HttpClient,
    ) {
      public suspend operator fun invoke(
        since: Instant? = null,
        perPage: Long? = 30L,
        page: Long? = 1L,
      ): Response {
        val response = client.get("gists/starred") {
          if (since != null) {
            this.url.encodedParameters.append("since", since.toString().encodeURLParameter())
          }
          if (perPage != null) {
            this.url.encodedParameters.append("per_page", perPage.toString())
          }
          if (page != null) {
            this.url.encodedParameters.append("page", page.toString())
          }
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          401 -> Response.Unauthorized(response.body())
          403 -> Response.Forbidden(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: List<BaseGist>,
        ) : Response

        public data object NotModified : Response

        public data class Unauthorized(
          public val `value`: BasicError,
        ) : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response
      }
    }
  }

  public class GistIdPath internal constructor(
    private val client: HttpClient,
    private val gistId: String,
  ) {
    public val delete: Delete = Delete(client, gistId)

    public val `get`: Get = Get(client, gistId)

    public val patch: Patch = Patch(client, gistId)

    public val comments: Comments = Comments(client, gistId)

    public val commits: Commits = Commits(client, gistId)

    public val forks: Forks = Forks(client, gistId)

    public val star: Star = Star(client, gistId)

    public fun sha(sha: String): ShaPath = ShaPath(client, gistId, sha)

    public class Delete internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.delete("gists/${this.gistId.encodeURLParameter()}")
        return when (response.status.value) {
          204 -> Response.NoContent
          304 -> Response.NotModified
          403 -> Response.Forbidden(response.body())
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data object NoContent : Response

        public data object NotModified : Response

        public data class Forbidden(
          public val `value`: BasicError,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Get internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public suspend operator fun invoke(): Response {
        val response = client.get("gists/${this.gistId.encodeURLParameter()}")
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          304 -> Response.NotModified
          403 -> response.body<Response.Forbidden>()
          404 -> Response.NotFound(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      public sealed interface Response {
        public data class Ok(
          public val `value`: GistSimple,
        ) : Response

        public data object NotModified : Response

        @OptIn(ExperimentalSerializationApi::class)
        @Serializable
        public data class Forbidden(
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val block: Block? = null,
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val message: String? = null,
          @SerialName("documentation_url")
          @EncodeDefault(EncodeDefault.Mode.NEVER)
          public val documentationUrl: String? = null,
        ) : Response {
          @OptIn(ExperimentalSerializationApi::class)
          @Serializable
          public data class Block(
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val reason: String? = null,
            @SerialName("created_at")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val createdAt: String? = null,
            @SerialName("html_url")
            @EncodeDefault(EncodeDefault.Mode.NEVER)
            public val htmlUrl: Optional<String> = Optional.Absent,
          )
        }

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response
      }
    }

    public class Patch internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public suspend operator fun invoke(description: String? = null, files: Map<String, Files?>? = null): Response {
        val response = client.patch("gists/${this.gistId.encodeURLParameter()}") {
          contentType(ContentType.Application.Json)
          setBody(Body(description = description, files = files))
        }
        return when (response.status.value) {
          200 -> Response.Ok(response.body())
          404 -> Response.NotFound(response.body())
          422 -> Response.UnprocessableEntity(response.body())
          else -> throw ResponseException(response, "")
        }
      }

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      public data class Files(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val content: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val filename: Optional<String> = Optional.Absent,
      )

      @OptIn(ExperimentalSerializationApi::class)
      @Serializable
      internal data class Body(
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val description: String? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        public val files: Map<String, Files?>? = null,
      )

      public sealed interface Response {
        public data class Ok(
          public val `value`: GistSimple,
        ) : Response

        public data class NotFound(
          public val `value`: BasicError,
        ) : Response

        public data class UnprocessableEntity(
          public val `value`: ValidationError,
        ) : Response
      }
    }

    public class Comments internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public val `get`: Get = Get(client, gistId)

      public val post: Post = Post(client, gistId)

      public fun commentId(commentId: Long): CommentIdPath = CommentIdPath(client, gistId, commentId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("gists/${this.gistId.encodeURLParameter()}/comments") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<GistComment>,
          ) : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(body: String): Response {
          val response = client.post("gists/${this.gistId.encodeURLParameter()}/comments") {
            contentType(ContentType.Application.Json)
            setBody(Body(body = body))
          }
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        @Serializable
        internal data class Body(
          public val body: String,
        )

        public sealed interface Response {
          public data class Created(
            public val `value`: GistComment,
          ) : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class CommentIdPath internal constructor(
        private val client: HttpClient,
        private val gistId: String,
        private val commentId: Long,
      ) {
        public val delete: Delete = Delete(client, gistId, commentId)

        public val `get`: Get = Get(client, gistId, commentId)

        public val patch: Patch = Patch(client, gistId, commentId)

        public class Delete internal constructor(
          private val client: HttpClient,
          private val gistId: String,
          private val commentId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.delete("gists/${this.gistId.encodeURLParameter()}/comments/${this.commentId}")
            return when (response.status.value) {
              204 -> Response.NoContent
              304 -> Response.NotModified
              403 -> Response.Forbidden(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data object NoContent : Response

            public data object NotModified : Response

            public data class Forbidden(
              public val `value`: BasicError,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Get internal constructor(
          private val client: HttpClient,
          private val gistId: String,
          private val commentId: Long,
        ) {
          public suspend operator fun invoke(): Response {
            val response = client.get("gists/${this.gistId.encodeURLParameter()}/comments/${this.commentId}")
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              304 -> Response.NotModified
              403 -> response.body<Response.Forbidden>()
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          public sealed interface Response {
            public data class Ok(
              public val `value`: GistComment,
            ) : Response

            public data object NotModified : Response

            @OptIn(ExperimentalSerializationApi::class)
            @Serializable
            public data class Forbidden(
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val block: Block? = null,
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val message: String? = null,
              @SerialName("documentation_url")
              @EncodeDefault(EncodeDefault.Mode.NEVER)
              public val documentationUrl: String? = null,
            ) : Response {
              @OptIn(ExperimentalSerializationApi::class)
              @Serializable
              public data class Block(
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val reason: String? = null,
                @SerialName("created_at")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val createdAt: String? = null,
                @SerialName("html_url")
                @EncodeDefault(EncodeDefault.Mode.NEVER)
                public val htmlUrl: Optional<String> = Optional.Absent,
              )
            }

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }

        public class Patch internal constructor(
          private val client: HttpClient,
          private val gistId: String,
          private val commentId: Long,
        ) {
          public suspend operator fun invoke(body: String): Response {
            val response = client.patch("gists/${this.gistId.encodeURLParameter()}/comments/${this.commentId}") {
              contentType(ContentType.Application.Json)
              setBody(Body(body = body))
            }
            return when (response.status.value) {
              200 -> Response.Ok(response.body())
              404 -> Response.NotFound(response.body())
              else -> throw ResponseException(response, "")
            }
          }

          @Serializable
          internal data class Body(
            public val body: String,
          )

          public sealed interface Response {
            public data class Ok(
              public val `value`: GistComment,
            ) : Response

            public data class NotFound(
              public val `value`: BasicError,
            ) : Response
          }
        }
      }
    }

    public class Commits internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public val `get`: Get = Get(client, gistId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("gists/${this.gistId.encodeURLParameter()}/commits") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<GistCommit>,
          ) : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }

    public class Forks internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public val `get`: Get = Get(client, gistId)

      public val post: Post = Post(client, gistId)

      public class Get internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(perPage: Long? = 30L, page: Long? = 1L): Response {
          val response = client.get("gists/${this.gistId.encodeURLParameter()}/forks") {
            if (perPage != null) {
              this.url.encodedParameters.append("per_page", perPage.toString())
            }
            if (page != null) {
              this.url.encodedParameters.append("page", page.toString())
            }
          }
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: List<GistSimple>,
          ) : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Post internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.post("gists/${this.gistId.encodeURLParameter()}/forks")
          return when (response.status.value) {
            201 -> Response.Created(response.body())
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Created(
            public val `value`: BaseGist,
          ) : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }
    }

    public class Star internal constructor(
      private val client: HttpClient,
      private val gistId: String,
    ) {
      public val delete: Delete = Delete(client, gistId)

      public val `get`: Get = Get(client, gistId)

      public val put: Put = Put(client, gistId)

      public class Delete internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.delete("gists/${this.gistId.encodeURLParameter()}/star")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }

      public class Get internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("gists/${this.gistId.encodeURLParameter()}/star")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data object NotFound : Response
        }
      }

      public class Put internal constructor(
        private val client: HttpClient,
        private val gistId: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.put("gists/${this.gistId.encodeURLParameter()}/star")
          return when (response.status.value) {
            204 -> Response.NoContent
            304 -> Response.NotModified
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data object NoContent : Response

          public data object NotModified : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response
        }
      }
    }

    public class ShaPath internal constructor(
      private val client: HttpClient,
      private val gistId: String,
      private val sha: String,
    ) {
      public val `get`: Get = Get(client, gistId, sha)

      public class Get internal constructor(
        private val client: HttpClient,
        private val gistId: String,
        private val sha: String,
      ) {
        public suspend operator fun invoke(): Response {
          val response = client.get("gists/${this.gistId.encodeURLParameter()}/${this.sha.encodeURLParameter()}")
          return when (response.status.value) {
            200 -> Response.Ok(response.body())
            403 -> Response.Forbidden(response.body())
            404 -> Response.NotFound(response.body())
            422 -> Response.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "")
          }
        }

        public sealed interface Response {
          public data class Ok(
            public val `value`: GistSimple,
          ) : Response

          public data class Forbidden(
            public val `value`: BasicError,
          ) : Response

          public data class NotFound(
            public val `value`: BasicError,
          ) : Response

          public data class UnprocessableEntity(
            public val `value`: ValidationError,
          ) : Response
        }
      }
    }
  }
}
