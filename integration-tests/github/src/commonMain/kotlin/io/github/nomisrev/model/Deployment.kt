package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement
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
import kotlinx.serialization.json.JsonDecoder

@Serializable
data class Deployment(
    val url: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val sha: String,
    val ref: String,
    val task: String,
    val payload: Payload,
    @SerialName("original_environment") val originalEnvironment: String? = null,
    val environment: String,
    val description: String?,
    val creator: NullableSimpleUser?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("statuses_url") val statusesUrl: String,
    @SerialName("repository_url") val repositoryUrl: String,
    @SerialName("transient_environment") val transientEnvironment: Boolean? = null,
    @SerialName("production_environment") val productionEnvironment: Boolean? = null,
    @SerialName("performed_via_github_app") val performedViaGithubApp: NullableIntegration? = null,
) {
    @Serializable(with = Payload.Serializer::class)
    sealed interface Payload {
        @Serializable
        @JvmInline
        value class CaseJsonElement(val value: JsonElement) : Payload

        @Serializable
        @JvmInline
        value class CaseString(val value: String) : Payload

        object Serializer : KSerializer<Payload> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.Deployment.Payload", PolymorphicKind.SEALED) {
                    element("CaseJsonElement", JsonElement.serializer().descriptor)
                    element("CaseString", String.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Payload {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
                    CaseJsonElement::class to { CaseJsonElement(decodeFromJsonElement(JsonElement.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Payload) = when(value) {
                is CaseJsonElement -> encoder.encodeSerializableValue(JsonElement.serializer(), value.value)
                is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
            }
        }
    }
}
