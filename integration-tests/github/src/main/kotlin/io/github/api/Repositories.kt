package io.github.api

import io.github.model.BasicError
import io.github.model.IssueFieldValue
import io.github.model.MinimalRepository
import io.github.model.ValidationError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.`get`
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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

public class Repositories internal constructor(
  private val client: HttpClient,
) {
  public val `get`: Get = Get(client)

  public fun repositoryId(repositoryId: Long): RepositoryIdPath = RepositoryIdPath(client, repositoryId)

  public class Get internal constructor(
    private val client: HttpClient,
  ) {
    public suspend operator fun invoke(since: Long? = null): Response {
      val response = client.get("/repositories") {
        since?.let { parameter("since", it) }
      }
      return when (response.status.value) {
        200 -> Response.Ok(response.body())
        304 -> Response.NotModified
        422 -> Response.UnprocessableEntity(response.body())
        else -> throw ResponseException(response, "")
      }
    }

    public sealed interface Response {
      public data class Ok(
        public val `value`: List<MinimalRepository>,
      ) : Response

      public data object NotModified : Response

      public data class UnprocessableEntity(
        public val `value`: ValidationError,
      ) : Response
    }
  }

  public class RepositoryIdPath internal constructor(
    private val client: HttpClient,
    private val repositoryId: Long,
  ) {
    public val issues: Issues = Issues(client, repositoryId)

    public class Issues internal constructor(
      private val client: HttpClient,
      private val repositoryId: Long,
    ) {
      public fun issueNumber(issueNumber: Long): IssueNumberPath = IssueNumberPath(client, repositoryId, issueNumber)

      public class IssueNumberPath internal constructor(
        private val client: HttpClient,
        private val repositoryId: Long,
        private val issueNumber: Long,
      ) {
        public val issueFieldValues: IssueFieldValues =
            IssueFieldValues(client, repositoryId, issueNumber)

        public class IssueFieldValues internal constructor(
          private val client: HttpClient,
          private val repositoryId: Long,
          private val issueNumber: Long,
        ) {
          public val post: Post = Post(client, repositoryId, issueNumber)

          public val put: Put = Put(client, repositoryId, issueNumber)

          public fun issueFieldId(issueFieldId: Long): IssueFieldIdPath = IssueFieldIdPath(client, repositoryId, issueNumber, issueFieldId)

          public class Post internal constructor(
            private val client: HttpClient,
            private val repositoryId: Long,
            private val issueNumber: Long,
          ) {
            public suspend operator fun invoke(issueFieldValues: List<IssueFieldValues>? = null): Response {
              val response = client.post("/repositories/$repositoryId/issues/$issueNumber/issue-field-values") {
                contentType(ContentType.Application.Json)
                setBody(Body(issueFieldValues = issueFieldValues))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public data class IssueFieldValues(
              @SerialName("field_id")
              public val fieldId: Long,
              public val `value`: Value,
            ) {
              /**
               * The value to set for the field. The type depends on the field's data type:
               * - For text fields: provide a string value
               * - For single_select fields: provide the option name as a string (must match an existing option)
               * - For number fields: provide a numeric value
               * - For date fields: provide an ISO 8601 date string
               */
              @Serializable(with = Value.Serializer::class)
              public sealed interface Value {
                @Serializable
                @JvmInline
                public value class CaseString(
                  public val `value`: String,
                ) : Value

                @Serializable
                @JvmInline
                public value class CaseDouble(
                  public val `value`: Double,
                ) : Value

                public object Serializer : KSerializer<Value> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Repositories.RepositoryIdPath.Issues.IssueNumberPath.IssueFieldValues.Post.IssueFieldValues.Value", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseDouble", Double.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Value {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: Value) {
                    when(value) {
                      is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                      is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                    }
                  }
                }
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("issue_field_values")
              public val issueFieldValues: List<IssueFieldValues>? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<IssueFieldValue>,
              ) : Response

              public data class BadRequest(
                public val `value`: BasicError,
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class Put internal constructor(
            private val client: HttpClient,
            private val repositoryId: Long,
            private val issueNumber: Long,
          ) {
            public suspend operator fun invoke(issueFieldValues: List<IssueFieldValues>? = null): Response {
              val response = client.put("/repositories/$repositoryId/issues/$issueNumber/issue-field-values") {
                contentType(ContentType.Application.Json)
                setBody(Body(issueFieldValues = issueFieldValues))
              }
              return when (response.status.value) {
                200 -> Response.Ok(response.body())
                400 -> Response.BadRequest(response.body())
                403 -> Response.Forbidden(response.body())
                404 -> Response.NotFound(response.body())
                422 -> Response.UnprocessableEntity(response.body())
                503 -> response.body<Response.ServiceUnavailable>()
                else -> throw ResponseException(response, "")
              }
            }

            @Serializable
            public data class IssueFieldValues(
              @SerialName("field_id")
              public val fieldId: Long,
              public val `value`: Value,
            ) {
              /**
               * The value to set for the field. The type depends on the field's data type:
               * - For text fields: provide a string value
               * - For single_select fields: provide the option name as a string (must match an existing option)
               * - For number fields: provide a numeric value
               * - For date fields: provide an ISO 8601 date string
               */
              @Serializable(with = Value.Serializer::class)
              public sealed interface Value {
                @Serializable
                @JvmInline
                public value class CaseString(
                  public val `value`: String,
                ) : Value

                @Serializable
                @JvmInline
                public value class CaseDouble(
                  public val `value`: Double,
                ) : Value

                public object Serializer : KSerializer<Value> {
                  @OptIn(
                    InternalSerializationApi::class,
                    ExperimentalSerializationApi::class,
                  )
                  override val descriptor: SerialDescriptor =
                      buildSerialDescriptor("io.github.api.Repositories.RepositoryIdPath.Issues.IssueNumberPath.IssueFieldValues.Put.IssueFieldValues.Value", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseDouble", Double.serializer().descriptor)
                  }

                  override fun deserialize(decoder: Decoder): Value {
                    val value = decoder.decodeSerializableValue(JsonElement.serializer())
                    val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                    return json.attemptDeserialize(
                      value,
                      CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
                      CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    )
                  }

                  override fun serialize(encoder: Encoder, `value`: Value) {
                    when(value) {
                      is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                      is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                    }
                  }
                }
              }
            }

            @JvmInline
            @Serializable
            internal value class Body(
              @SerialName("issue_field_values")
              public val issueFieldValues: List<IssueFieldValues>? = null,
            )

            public sealed interface Response {
              public data class Ok(
                public val `value`: List<IssueFieldValue>,
              ) : Response

              public data class BadRequest(
                public val `value`: BasicError,
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

              @Serializable
              public data class ServiceUnavailable(
                public val code: String? = null,
                public val message: String? = null,
                @SerialName("documentation_url")
                public val documentationUrl: String? = null,
              ) : Response
            }
          }

          public class IssueFieldIdPath internal constructor(
            private val client: HttpClient,
            private val repositoryId: Long,
            private val issueNumber: Long,
            private val issueFieldId: Long,
          ) {
            public val delete: Delete = Delete(client, repositoryId, issueNumber, issueFieldId)

            public class Delete internal constructor(
              private val client: HttpClient,
              private val repositoryId: Long,
              private val issueNumber: Long,
              private val issueFieldId: Long,
            ) {
              public suspend operator fun invoke(): Response {
                val response = client.delete("/repositories/$repositoryId/issues/$issueNumber/issue-field-values/$issueFieldId")
                return when (response.status.value) {
                  204 -> Response.NoContent
                  403 -> Response.Forbidden(response.body())
                  404 -> Response.NotFound(response.body())
                  422 -> Response.UnprocessableEntity(response.body())
                  503 -> response.body<Response.ServiceUnavailable>()
                  else -> throw ResponseException(response, "")
                }
              }

              public sealed interface Response {
                public data object NoContent : Response

                public data class Forbidden(
                  public val `value`: BasicError,
                ) : Response

                public data class NotFound(
                  public val `value`: BasicError,
                ) : Response

                public data class UnprocessableEntity(
                  public val `value`: ValidationError,
                ) : Response

                @Serializable
                public data class ServiceUnavailable(
                  public val code: String? = null,
                  public val message: String? = null,
                  @SerialName("documentation_url")
                  public val documentationUrl: String? = null,
                ) : Response
              }
            }
          }
        }
      }
    }
  }
}
