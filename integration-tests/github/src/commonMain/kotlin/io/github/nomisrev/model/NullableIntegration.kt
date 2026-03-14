package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.nullable

@Serializable
data class NullableIntegration(
    val id: Long,
    val slug: String? = null,
    @SerialName("node_id") val nodeId: String,
    @SerialName("client_id") val clientId: String? = null,
    val owner: Owner,
    val name: String,
    val description: String?,
    @SerialName("external_url") val externalUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val permissions: Permissions,
    val events: List<String>,
    @SerialName("installations_count") val installationsCount: Long? = null,
) {
    @Serializable(with = Owner.Serializer::class)
    sealed interface Owner {
        @Serializable
        @JvmInline
        value class CaseSimpleUser(val value: SimpleUser) : Owner

        @Serializable
        @JvmInline
        value class CaseEnterprise(val value: Enterprise) : Owner

        object Serializer : KSerializer<Owner> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.NullableIntegration.Owner", PolymorphicKind.SEALED) {
                    element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                    element("CaseEnterprise", Enterprise.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Owner {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                    CaseEnterprise::class to { CaseEnterprise(decodeFromJsonElement(Enterprise.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Owner) = when(value) {
                is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @KeepGeneratedSerializer
    @Serializable(with = Permissions.Serializer::class)
    data class Permissions(
        val issues: String? = null,
        val checks: String? = null,
        val metadata: String? = null,
        val contents: String? = null,
        val deployments: String? = null,
        val additional: Map<String, String>? = null,
    ) {

        object Serializer : KSerializer<Permissions> {
            override val descriptor: SerialDescriptor = generatedSerializer().descriptor

            override fun serialize(encoder: Encoder, value: Permissions) {
                val json = (encoder as JsonEncoder).json
                return encoder.encodeSerializableValue(
                    JsonObject.serializer(),
                    buildJsonObject {
                        put("issues", json.encodeToJsonElement(String.serializer().nullable, value.issues))
                        put("checks", json.encodeToJsonElement(String.serializer().nullable, value.checks))
                        put("metadata", json.encodeToJsonElement(String.serializer().nullable, value.metadata))
                        put("contents", json.encodeToJsonElement(String.serializer().nullable, value.contents))
                        put("deployments", json.encodeToJsonElement(String.serializer().nullable, value.deployments))
                        value.additional?.forEach { (key, value) ->
                            put(key, json.encodeToJsonElement(String.serializer(), value))
                        }
                    })
            }

            override fun deserialize(decoder: Decoder): Permissions {
                val json = (decoder as JsonDecoder).json
                val element = decoder.decodeSerializableValue(JsonObject.serializer())
                val names = setOf("issues", "checks", "metadata", "contents", "deployments")
                require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
                return Permissions(
                    issues = if(element.containsKey("issues")) json.decodeFromJsonElement(String.serializer().nullable, element["issues"]!!) else null,
                    checks = if(element.containsKey("checks")) json.decodeFromJsonElement(String.serializer().nullable, element["checks"]!!) else null,
                    metadata = if(element.containsKey("metadata")) json.decodeFromJsonElement(String.serializer().nullable, element["metadata"]!!) else null,
                    contents = if(element.containsKey("contents")) json.decodeFromJsonElement(String.serializer().nullable, element["contents"]!!) else null,
                    deployments = if(element.containsKey("deployments")) json.decodeFromJsonElement(String.serializer().nullable, element["deployments"]!!) else null,
                    additional = (element - names)
                        .mapValues { (_, value) -> json.decodeFromJsonElement(String.serializer(), value) }
                )
            }
        }
    }
}
