package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Request to install an integration on a target
 */
@Serializable
public data class IntegrationInstallationRequest(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String? = null,
  public val account: Account,
  public val requester: SimpleUser,
  @SerialName("created_at")
  public val createdAt: Instant,
) {
  @Serializable(with = Account.Serializer::class)
  public sealed interface Account {
    @Serializable
    @JvmInline
    public value class CaseSimpleUser(
      public val `value`: SimpleUser,
    ) : Account

    @Serializable
    @JvmInline
    public value class CaseEnterprise(
      public val `value`: Enterprise,
    ) : Account

    public object Serializer : KSerializer<Account> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.IntegrationInstallationRequest.Account", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: Account) {
        when(value) {
          is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
          is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
        }
      }
    }
  }
}
