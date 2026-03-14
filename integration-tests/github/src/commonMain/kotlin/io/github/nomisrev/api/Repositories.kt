package io.github.nomisrev.api

import io.github.nomisrev.model.MinimalRepository
import io.github.nomisrev.model.ValidationError
import kotlinx.serialization.Serializable
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
import io.github.nomisrev.model.IssueFieldValue
import io.github.nomisrev.model.BasicError
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.http.ContentType
import io.ktor.client.request.setBody
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.contentType

interface Repositories {
    val issues: Repositories.Issues

    sealed interface ReposListPublicResult {
        data class OK(val value: List<MinimalRepository>) : ReposListPublicResult

        data object NotModified : ReposListPublicResult

        data class UnprocessableEntity(val value: ValidationError) : ReposListPublicResult
    }

    suspend fun reposListPublic(
        since: Long? = null,
    ): ReposListPublicResult

    interface Issues {
        val issueFieldValues: Repositories.Issues.IssueFieldValues

        interface IssueFieldValues {
            @Serializable
            @JvmInline
            value class IssuesAddIssueFieldValuesBody(@SerialName("issue_field_values") val issueFieldValues: List<IssueFieldValues>? = null) {
                @Serializable
                data class IssueFieldValues(@SerialName("field_id") val fieldId: Long, val value: Value) {
                    @Serializable(with = Value.Serializer::class)
                    sealed interface Value {
                        @Serializable
                        @JvmInline
                        value class CaseString(val value: String) : Value

                        @Serializable
                        @JvmInline
                        value class CaseDouble(val value: Double) : Value

                        object Serializer : KSerializer<Value> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesBody.IssueFieldValues.Value", PolymorphicKind.SEALED) {
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

                            override fun serialize(encoder: Encoder, value: Value) = when(value) {
                                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                            }
                        }
                    }
                }
            }


            @Serializable
            data class IssuesAddIssueFieldValuesResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            data class IssuesDeleteIssueFieldValueResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )


            @Serializable
            @JvmInline
            value class IssuesSetIssueFieldValuesBody(@SerialName("issue_field_values") val issueFieldValues: List<IssueFieldValues>? = null) {
                @Serializable
                data class IssueFieldValues(@SerialName("field_id") val fieldId: Long, val value: Value) {
                    @Serializable(with = Value.Serializer::class)
                    sealed interface Value {
                        @Serializable
                        @JvmInline
                        value class CaseString(val value: String) : Value

                        @Serializable
                        @JvmInline
                        value class CaseDouble(val value: Double) : Value

                        object Serializer : KSerializer<Value> {
                            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
                            override val descriptor: SerialDescriptor =
                                buildSerialDescriptor("io.github.nomisrev.api.Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesBody.IssueFieldValues.Value", PolymorphicKind.SEALED) {
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

                            override fun serialize(encoder: Encoder, value: Value) = when(value) {
                                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                                is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
                            }
                        }
                    }
                }
            }


            @Serializable
            data class IssuesSetIssueFieldValuesResponse(
                val code: String? = null,
                val message: String? = null,
                @SerialName("documentation_url") val documentationUrl: String? = null,
            )

            sealed interface IssuesSetIssueFieldValuesResult {
                data class OK(val value: List<IssueFieldValue>) : IssuesSetIssueFieldValuesResult

                data class BadRequest(val value: BasicError) : IssuesSetIssueFieldValuesResult

                data class Forbidden(val value: BasicError) : IssuesSetIssueFieldValuesResult

                data class NotFound(val value: BasicError) : IssuesSetIssueFieldValuesResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesSetIssueFieldValuesResult

                data class ServiceUnavailable(val value: IssuesSetIssueFieldValuesResponse) : IssuesSetIssueFieldValuesResult
            }

            suspend fun issuesSetIssueFieldValues(
                repositoryId: Long,
                issueNumber: Long,
                body: IssuesSetIssueFieldValuesBody,
            ): IssuesSetIssueFieldValuesResult

            sealed interface IssuesAddIssueFieldValuesResult {
                data class OK(val value: List<IssueFieldValue>) : IssuesAddIssueFieldValuesResult

                data class BadRequest(val value: BasicError) : IssuesAddIssueFieldValuesResult

                data class Forbidden(val value: BasicError) : IssuesAddIssueFieldValuesResult

                data class NotFound(val value: BasicError) : IssuesAddIssueFieldValuesResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesAddIssueFieldValuesResult

                data class ServiceUnavailable(val value: IssuesAddIssueFieldValuesResponse) : IssuesAddIssueFieldValuesResult
            }

            suspend fun issuesAddIssueFieldValues(
                repositoryId: Long,
                issueNumber: Long,
                body: IssuesAddIssueFieldValuesBody,
            ): IssuesAddIssueFieldValuesResult

            sealed interface IssuesDeleteIssueFieldValueResult {
                data object NoContent : IssuesDeleteIssueFieldValueResult

                data class Forbidden(val value: BasicError) : IssuesDeleteIssueFieldValueResult

                data class NotFound(val value: BasicError) : IssuesDeleteIssueFieldValueResult

                data class UnprocessableEntity(val value: ValidationError) : IssuesDeleteIssueFieldValueResult

                data class ServiceUnavailable(val value: IssuesDeleteIssueFieldValueResponse) : IssuesDeleteIssueFieldValueResult
            }

            suspend fun issuesDeleteIssueFieldValue(
                repositoryId: Long,
                issueNumber: Long,
                issueFieldId: Long,
            ): IssuesDeleteIssueFieldValueResult
        }
    }
}

internal class KtorRepositories(private val client: HttpClient) : Repositories {
    override val issues: Repositories.Issues = KtorRepositoriesIssues(client)

    override suspend fun reposListPublic(since: Long?): Repositories.ReposListPublicResult {
        val response = client.get("/repositories") {
            since?.let { parameter("since", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repositories.ReposListPublicResult.OK(response.body())
            HttpStatusCode.NotModified -> Repositories.ReposListPublicResult.NotModified
            HttpStatusCode.UnprocessableEntity -> Repositories.ReposListPublicResult.UnprocessableEntity(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}

internal class KtorRepositoriesIssues(private val client: HttpClient) : Repositories.Issues {
    override val issueFieldValues: Repositories.Issues.IssueFieldValues = KtorRepositoriesIssuesIssueFieldValues(client)
}

internal class KtorRepositoriesIssuesIssueFieldValues(private val client: HttpClient) : Repositories.Issues.IssueFieldValues {
    override suspend fun issuesSetIssueFieldValues(repositoryId: Long, issueNumber: Long, body: Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesBody): Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult {
        val response = client.put("/repositories/$repositoryId/issues/$issueNumber/issue-field-values") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repositories.Issues.IssueFieldValues.IssuesSetIssueFieldValuesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesAddIssueFieldValues(repositoryId: Long, issueNumber: Long, body: Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesBody): Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult {
        val response = client.post("/repositories/$repositoryId/issues/$issueNumber/issue-field-values") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult.OK(response.body())
            HttpStatusCode.BadRequest -> Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult.BadRequest(response.body())
            HttpStatusCode.Forbidden -> Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repositories.Issues.IssueFieldValues.IssuesAddIssueFieldValuesResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun issuesDeleteIssueFieldValue(repositoryId: Long, issueNumber: Long, issueFieldId: Long): Repositories.Issues.IssueFieldValues.IssuesDeleteIssueFieldValueResult {
        val response = client.delete("/repositories/$repositoryId/issues/$issueNumber/issue-field-values/$issueFieldId")
        return when (response.status) {
            HttpStatusCode.NoContent -> Repositories.Issues.IssueFieldValues.IssuesDeleteIssueFieldValueResult.NoContent
            HttpStatusCode.Forbidden -> Repositories.Issues.IssueFieldValues.IssuesDeleteIssueFieldValueResult.Forbidden(response.body())
            HttpStatusCode.NotFound -> Repositories.Issues.IssueFieldValues.IssuesDeleteIssueFieldValueResult.NotFound(response.body())
            HttpStatusCode.UnprocessableEntity -> Repositories.Issues.IssueFieldValues.IssuesDeleteIssueFieldValueResult.UnprocessableEntity(response.body())
            HttpStatusCode.ServiceUnavailable -> Repositories.Issues.IssueFieldValues.IssuesDeleteIssueFieldValueResult.ServiceUnavailable(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
