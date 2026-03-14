package io.github.nomisrev.api

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import io.github.nomisrev.model.attemptDeserialize
import kotlinx.serialization.SerialName
import io.github.nomisrev.model.GlobalAdvisoryResponse
import io.github.nomisrev.model.ValidationErrorSimple
import io.github.nomisrev.model.BasicError
import io.github.nomisrev.model.SecurityAdvisoryEcosystems
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface Advisories {
    @Serializable(with = Affects.Serializer::class)
    sealed interface Affects {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Affects

        @Serializable
        @JvmInline
        value class CaseStrings(val value: List<String>) : Affects

        object Serializer : KSerializer<Affects> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.api.Advisories.Affects", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                }

            override fun deserialize(decoder: Decoder): Affects {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Affects) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
            }
        }
    }


    @Serializable(with = Cwes.Serializer::class)
    sealed interface Cwes {
        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Cwes

        @Serializable
        @JvmInline
        value class CaseStrings(val value: List<String>) : Cwes

        object Serializer : KSerializer<Cwes> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.api.Advisories.Cwes", PolymorphicKind.SEALED) {
                    element("CaseString", String.serializer().descriptor)
                    element("CaseStrings", ListSerializer(String.serializer()).descriptor)
                }

            override fun deserialize(decoder: Decoder): Cwes {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Cwes) = when(value) {
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
                is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
            }
        }
    }


    @Serializable
    enum class Direction {
        @SerialName("asc") Asc, @SerialName("desc") Desc;
    }


    @Serializable
    enum class Severity {
        @SerialName("unknown")
        Unknown,
        @SerialName("low")
        Low,
        @SerialName("medium")
        Medium,
        @SerialName("high")
        High,
        @SerialName("critical")
        Critical;
    }


    @Serializable
    enum class Sort {
        @SerialName("updated")
        Updated,
        @SerialName("published")
        Published,
        @SerialName("epss_percentage")
        EpssPercentage,
        @SerialName("epss_percentile")
        EpssPercentile;
    }


    @Serializable
    enum class Type {
        @SerialName("reviewed") Reviewed, @SerialName("malware") Malware, @SerialName("unreviewed") Unreviewed;
    }

    sealed interface SecurityAdvisoriesListGlobalAdvisoriesResult {
        data class OK(val value: List<GlobalAdvisoryResponse>) : SecurityAdvisoriesListGlobalAdvisoriesResult

        data class UnprocessableEntity(val value: ValidationErrorSimple) : SecurityAdvisoriesListGlobalAdvisoriesResult

        data class TooManyRequests(val value: BasicError) : SecurityAdvisoriesListGlobalAdvisoriesResult
    }

    suspend fun securityAdvisoriesListGlobalAdvisories(
        direction: Direction = Direction.Desc,
        perPage: Long = 30L,
        sort: Sort = Sort.Published,
        type: Type = Type.Reviewed,
        affects: Affects? = null,
        after: String? = null,
        before: String? = null,
        cveId: String? = null,
        cwes: Cwes? = null,
        ecosystem: SecurityAdvisoryEcosystems? = null,
        epssPercentage: String? = null,
        epssPercentile: String? = null,
        ghsaId: String? = null,
        isWithdrawn: Boolean? = null,
        modified: String? = null,
        published: String? = null,
        severity: Severity? = null,
        updated: String? = null,
    ): SecurityAdvisoriesListGlobalAdvisoriesResult

    sealed interface SecurityAdvisoriesGetGlobalAdvisoryResult {
        data class OK(val value: GlobalAdvisoryResponse) : SecurityAdvisoriesGetGlobalAdvisoryResult

        data class NotFound(val value: BasicError) : SecurityAdvisoriesGetGlobalAdvisoryResult
    }

    suspend fun securityAdvisoriesGetGlobalAdvisory(
        ghsaId: String,
    ): SecurityAdvisoriesGetGlobalAdvisoryResult
}

internal class KtorAdvisories(private val client: HttpClient) : Advisories {
    override suspend fun securityAdvisoriesListGlobalAdvisories(direction: Advisories.Direction, perPage: Long, sort: Advisories.Sort, type: Advisories.Type, affects: Advisories.Affects?, after: String?, before: String?, cveId: String?, cwes: Advisories.Cwes?, ecosystem: SecurityAdvisoryEcosystems?, epssPercentage: String?, epssPercentile: String?, ghsaId: String?, isWithdrawn: Boolean?, modified: String?, published: String?, severity: Advisories.Severity?, updated: String?): Advisories.SecurityAdvisoriesListGlobalAdvisoriesResult {
        val response = client.get("/advisories") {
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("sort", sort)
            parameter("type", type)
            affects?.let { parameter("affects", it) }
            after?.let { parameter("after", it) }
            before?.let { parameter("before", it) }
            cveId?.let { parameter("cve_id", it) }
            cwes?.let { parameter("cwes", it) }
            ecosystem?.let { parameter("ecosystem", it) }
            epssPercentage?.let { parameter("epss_percentage", it) }
            epssPercentile?.let { parameter("epss_percentile", it) }
            ghsaId?.let { parameter("ghsa_id", it) }
            isWithdrawn?.let { parameter("is_withdrawn", it) }
            modified?.let { parameter("modified", it) }
            published?.let { parameter("published", it) }
            severity?.let { parameter("severity", it) }
            updated?.let { parameter("updated", it) }
        }
        return when (response.status) {
            HttpStatusCode.OK -> Advisories.SecurityAdvisoriesListGlobalAdvisoriesResult.OK(response.body())
            HttpStatusCode.UnprocessableEntity -> Advisories.SecurityAdvisoriesListGlobalAdvisoriesResult.UnprocessableEntity(response.body())
            HttpStatusCode.TooManyRequests -> Advisories.SecurityAdvisoriesListGlobalAdvisoriesResult.TooManyRequests(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }

    override suspend fun securityAdvisoriesGetGlobalAdvisory(ghsaId: String): Advisories.SecurityAdvisoriesGetGlobalAdvisoryResult {
        val response = client.get("/advisories/$ghsaId")
        return when (response.status) {
            HttpStatusCode.OK -> Advisories.SecurityAdvisoriesGetGlobalAdvisoryResult.OK(response.body())
            HttpStatusCode.NotFound -> Advisories.SecurityAdvisoriesGetGlobalAdvisoryResult.NotFound(response.body())
            else -> throw ResponseException(response, "Undocumented status code: ${response.status}")
        }
    }
}
