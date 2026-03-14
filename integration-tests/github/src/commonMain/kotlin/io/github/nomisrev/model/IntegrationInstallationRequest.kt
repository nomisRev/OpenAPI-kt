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

@Serializable
data class IntegrationInstallationRequest(
    val id: Long,
    @SerialName("node_id") val nodeId: String? = null,
    val account: Account,
    val requester: SimpleUser,
    @SerialName("created_at") val createdAt: LocalDateTime,
) {
    @Serializable(with = Account.Serializer::class)
    sealed interface Account {
        @Serializable
        @JvmInline
        value class CaseSimpleUser(val value: SimpleUser) : Account

        @Serializable
        @JvmInline
        value class CaseEnterprise(val value: Enterprise) : Account

        object Serializer : KSerializer<Account> {
            @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
            override val descriptor: SerialDescriptor =
                buildSerialDescriptor("io.github.nomisrev.model.IntegrationInstallationRequest.Account", PolymorphicKind.SEALED) {
                    element("CaseSimpleUser", SimpleUser.serializer().descriptor)
                    element("CaseEnterprise", Enterprise.serializer().descriptor)
                }

            override fun deserialize(decoder: Decoder): Account {
                val value = decoder.decodeSerializableValue(JsonElement.serializer())
                val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
                return json.attemptDeserialize(
                    value,
                    CaseSimpleUser::class to { CaseSimpleUser(decodeFromJsonElement(SimpleUser.serializer(), it)) },
                    CaseEnterprise::class to { CaseEnterprise(decodeFromJsonElement(Enterprise.serializer(), it)) },
                )
            }

            override fun serialize(encoder: Encoder, value: Account) = when(value) {
                is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
                is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
            }
        }
    }
}
