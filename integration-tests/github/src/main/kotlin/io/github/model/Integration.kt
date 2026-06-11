package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
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
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

/**
 * GitHub apps are a new way to extend GitHub. They can be installed directly on organizations and user accounts and granted access to specific repositories. They come with granular permissions and built-in webhooks. GitHub apps are first class actors within GitHub.
 */
@Serializable
public data class Integration(
  public val id: Long,
  public val slug: String? = null,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("client_id")
  public val clientId: String? = null,
  public val owner: Owner,
  public val name: String,
  public val description: String?,
  @SerialName("external_url")
  public val externalUrl: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val permissions: Permissions,
  public val events: List<String>,
  @SerialName("installations_count")
  public val installationsCount: Long? = null,
) {
  @Serializable(with = Owner.Serializer::class)
  public sealed interface Owner {
    @Serializable
    @JvmInline
    public value class CaseSimpleUser(
      public val `value`: SimpleUser,
    ) : Owner

    @Serializable
    @JvmInline
    public value class CaseEnterprise(
      public val `value`: Enterprise,
    ) : Owner

    public object Serializer : KSerializer<Owner> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.model.Integration.Owner", PolymorphicKind.SEALED) {
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

      override fun serialize(encoder: Encoder, `value`: Owner) {
        when(value) {
          is CaseSimpleUser -> encoder.encodeSerializableValue(SimpleUser.serializer(), value.value)
          is CaseEnterprise -> encoder.encodeSerializableValue(Enterprise.serializer(), value.value)
        }
      }
    }
  }

  /**
   * The set of permissions for the GitHub app
   */
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Permissions.Serializer::class)
  public data class Permissions(
    public val issues: String? = null,
    public val checks: String? = null,
    public val metadata: String? = null,
    public val contents: String? = null,
    public val deployments: String? = null,
    public val additional: Map<String, String>? = null,
  ) {
    public object Serializer : KSerializer<Permissions> {
      override val descriptor: SerialDescriptor = generatedSerializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Permissions) {
        val json = (encoder as JsonEncoder).json
        val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
        val content = mutableMapOf<String, JsonElement>()
        known.forEach {
          if (it.key != "additional") {
            content[it.key] = it.value
          }
        }
        value.additional?.forEach {
          content[it.key] = json.encodeToJsonElement(String.serializer(), it.value)
        }
        encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
      }

      override fun deserialize(decoder: Decoder): Permissions {
        val json = (decoder as JsonDecoder).json
        val element = decoder.decodeSerializableValue(JsonObject.serializer())
        val knownNames = setOf("issues", "checks", "metadata", "contents", "deployments")
        val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
        val additional = (element - knownNames)
          .mapValues { json.decodeFromJsonElement(String.serializer(), it.value) }
          .ifEmpty { null }
        return known.copy(additional = additional)
      }
    }
  }
}
